package com.jt.manager.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.service.PropertieService;
import com.jt.common.vo.PicUploadResult;

@Service
public class PicUploadService {
	
	@Autowired
	private PropertieService props;
	public PicUploadResult savePicUpload(MultipartFile uploadFile) throws IOException {
		/*
		 * 1.图片格式校验 
		 * 2.木马查杀 
		 * 3.生成真实存放地址和虚拟路径 
		 * 4.生成图片存放目录 
		 * 5.生成新的文件名称 
		 * 6.保存图片
		 * 7.生成一个picUploadResult对象来封装数据
		 */
		PicUploadResult result = new PicUploadResult();

		String oldFileName = uploadFile.getOriginalFilename();
		String extFileName = oldFileName.substring(oldFileName.lastIndexOf("."));
		// 正则判断文件后缀名是否符合要求的格式
		if (!extFileName.matches("^.*(.jpg|.gif|.jpeg|.png)$")) {
			result.setError(1);// 不符合设置状态1表示异常
		}
		// 设置回显图片大小
		BufferedImage bufferImage = ImageIO.read(uploadFile.getInputStream());
		result.setHeight(bufferImage.getHeight() + "");
		result.setWidth(bufferImage.getWidth() + "");
		// 生成有格式的路径
		String dir = "/images/" + new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + "/";
		// 相对路径
//		String urlPrefix = "http://image.jt.com" + dir;
		String urlPrefix = props.IMAGE_BASE_URL + dir;
		// 绝对路径    属性注入的解耦操作
//		String path = "E:/jt-upload" + dir;
		String path = props.REPOSITORY_PATH + dir;
		File dirs = new File(path);
		// 判断目录是否存在
		if (!dirs.exists()) {
			dirs.mkdirs();
		}
		// 生成新的文件名: 当前系统时间毫秒值+3位随机数
		String newImageName = System.currentTimeMillis() + RandomUtils.nextInt(100, 999) + extFileName;
		// 保存到磁盘操作
		uploadFile.transferTo(new File(path + newImageName));

		result.setUrl(urlPrefix + newImageName);

		return result;
	}
}
