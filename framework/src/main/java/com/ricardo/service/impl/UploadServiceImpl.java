package com.ricardo.service.impl;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.ricardo.constants.SystemConstants;
import com.ricardo.domain.ResponseResult;
import com.ricardo.enums.AppHttpCodeEnum;
import com.ricardo.exception.SystemException;
import com.ricardo.parameter.OSSParameter;
import com.ricardo.service.UploadService;
import com.ricardo.utils.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    private OSSParameter ossParameter;
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        //判断文件类型或者文件大小
        //获取原始文件名
        String originalFilename = img.getOriginalFilename();
        //判读大小
        if(!originalFilename.endsWith(".png")&&!originalFilename.endsWith(".jpg")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //满足，上传文件到oss
        String filePath = PathUtils.generateFilePath(originalFilename);
        String url=uploadOSS(img,filePath);//2023/3/23/id.png
        return ResponseResult.okResult(url);
    }

    private String uploadOSS(MultipartFile img, String filePath){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
//        String accessKey = ossTest.getAccessKey();
//        String secretKey = ossTest.getSecretKey();
//        String bucket = ossTest.getBucket();
//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;
        try {
//            byte[] uploadBytes = "C:\\Users\\shuaibaozhang\\OneDrive\\桌面\\编程背景.png".getBytes("utf-8");
            InputStream is= img.getInputStream();
            Auth auth = Auth.create(ossParameter.getAccessKey(), ossParameter.getSecretKey());
            String upToken = auth.uploadToken(ossParameter.getBucket());
            try {
                Response response = uploadManager.put(is, key, upToken,null,null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return "http://"+SystemConstants.CDN+"/"+key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "222";
    }
}
