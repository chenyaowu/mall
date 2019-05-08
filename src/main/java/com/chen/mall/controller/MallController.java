package com.chen.mall.controller;

import com.chen.mall.access.AccessLimit;
import com.chen.mall.rabbitmq.MQSender;
import com.chen.mall.rabbitmq.MallMessage;
import com.chen.mall.redis.GoodsKey;
import com.chen.mall.redis.MallKey;
import com.chen.mall.redis.OrderKey;
import com.chen.mall.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.chen.mall.domain.MallOrder;
import com.chen.mall.domain.MallUser;
import com.chen.mall.domain.OrderInfo;
import com.chen.mall.redis.RedisService;
import com.chen.mall.result.CodeMsg;
import com.chen.mall.service.GoodsService;
import com.chen.mall.service.MallService;
import com.chen.mall.service.MallUserService;
import com.chen.mall.service.OrderService;
import com.chen.mall.vo.GoodsVo;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/mall")
public class MallController implements InitializingBean {
	private static Logger log = LoggerFactory.getLogger(MallController.class);
	@Autowired
	MallUserService userService;

	@Autowired
	RedisService redisService;

	@Autowired
	GoodsService goodsService;

	@Autowired
	OrderService orderService;

	@Autowired
	MallService mallService;

	@Autowired
	MQSender sender;

	private HashMap<Long, Boolean> localOverMap =  new HashMap<>();

	/**
	 * 系统初始化
	 * */
	public void afterPropertiesSet() throws Exception {
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		if(goodsList == null) {
			return;
		}
		for(GoodsVo goods : goodsList) {
			redisService.set(GoodsKey.getMallGoodsStock, ""+goods.getId(), goods.getStockCount());
			localOverMap.put(goods.getId(), false);
		}
	}

	@RequestMapping(value="/reset", method=RequestMethod.GET)
	@ResponseBody
	public Result<Boolean> reset(Model model) {
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		for(GoodsVo goods : goodsList) {
			goods.setStockCount(10);
			redisService.set(GoodsKey.getMallGoodsStock, ""+goods.getId(), 10);
			localOverMap.put(goods.getId(), false);
		}
		redisService.delete(OrderKey.getMallOrderByUidGid);
		redisService.delete(MallKey.isGoodsOver);
		mallService.reset(goodsList);
		return Result.success(true);
	}


	@RequestMapping(value="/{path}/do_mall", method= RequestMethod.POST)
	@ResponseBody
	public Result<Integer> mall(Model model, MallUser user,
									 @RequestParam("goodsId")long goodsId,@PathVariable("path") String path) {
		model.addAttribute("user", user);
		if(user == null) {
			log.info("user is null");
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		//验证path
		boolean check = mallService.checkPath(user, goodsId, path);
		if(!check){
			return Result.error(CodeMsg.REQUEST_ILLEGAL);
		}
		//内存标记，减少redis访问
		boolean over = localOverMap.get(goodsId);
		if(over) {
			return Result.error(CodeMsg.MALL_OVER);
		}
		//预减库存
		long stock = redisService.decr(GoodsKey.getMallGoodsStock, ""+goodsId);//10
		if(stock < 0) {
			localOverMap.put(goodsId, true);
			return Result.error(CodeMsg.MALL_OVER);
		}
		//判断是否已经秒杀到了
		MallOrder order = orderService.getMallOrderByUserIdGoodsId(user.getId(), goodsId);
		if(order != null) {
			return Result.error(CodeMsg.REPEATE_MALL);
		}
		//入队
		MallMessage mm = new MallMessage();
		mm.setUser(user);
		mm.setGoodsId(goodsId);
		sender.sendMallMessage(mm);
		return Result.success(0);//排队中
    
	}

	/**
	 * orderId：成功
	 * -1：秒杀失败
	 * 0： 排队中
	 * */
	@RequestMapping(value="/result", method=RequestMethod.GET)
	@ResponseBody
	public Result<Long> mallResult(Model model,MallUser user,
									  @RequestParam("goodsId")long goodsId) {
		model.addAttribute("user", user);
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		long result  =mallService.getMallResult(user.getId(), goodsId);
		return Result.success(result);
	}

	@AccessLimit(seconds=5, maxCount=5, needLogin=true)
	@RequestMapping(value="/path", method=RequestMethod.GET)
	@ResponseBody
	public Result<String> getMallPath(HttpServletRequest request, MallUser user,
										 @RequestParam("goodsId")long goodsId,
										 @RequestParam(value="verifyCode", defaultValue="0")int verifyCode
	) {
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		boolean check = mallService.checkVerifyCode(user, goodsId, verifyCode);
		if(!check) {
			return Result.error(CodeMsg.REQUEST_ILLEGAL);
		}
		String path  =mallService.createMallPath(user, goodsId);
		return Result.success(path);
	}

	@RequestMapping(value="/verifyCode", method=RequestMethod.GET)
	@ResponseBody
	public Result<String> getMallVerifyCod(HttpServletResponse response, MallUser user,
											  @RequestParam("goodsId")long goodsId) {
		if(user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		try {
			BufferedImage image  = mallService.createVerifyCode(user, goodsId);
			OutputStream out = response.getOutputStream();
			ImageIO.write(image, "JPEG", out);
			out.flush();
			out.close();
			return null;
		}catch(Exception e) {
			e.printStackTrace();
			return Result.error(CodeMsg.MALL_FAIL);
		}
	}
}
