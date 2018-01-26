package com.jt.manager.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.common.vo.PicUploadResult;
import com.jt.manager.service.PicUploadService;

@Controller	
public class PicUploadController {
	
	private static final Logger log = Logger.getLogger(PicUploadController.class);
	@Autowired
	private PicUploadService picUploadService;
	@RequestMapping("pic/upload")
	@ResponseBody
	public PicUploadResult picUpload(MultipartFile uploadFile){
		PicUploadResult result=new PicUploadResult();
		try {
			result=picUploadService.savePicUpload(uploadFile);
			log.info("{上传成功}");
		} catch (Exception e) {
			//写错误日志
			log.error("{上传失败:"+e.getMessage()+"}");
			result.setError(1);
		}
		return result;
	}
}
