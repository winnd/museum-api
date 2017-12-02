package com.museum.api.core.controller;

import com.museum.api.common.annotation.Authorization;
import com.museum.api.common.constant.Constants;
import com.museum.api.common.controller.BaseController;
import com.museum.api.common.orm.model.FileResources;
import com.museum.api.common.vo.BaseModel;
import com.museum.api.core.service.FileService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequestMapping("file")
@Controller
public class FileController extends BaseController{

    private static final Logger logger = org.apache.log4j.Logger.getLogger(FileController.class);

    public static final Integer maxSize5M = 5 * 1024 * 1024; // 文件最大体积为5M
    public static final Integer maxSize10M = 10 * 1024 * 1024; // 文件最大体积为10M


    class FileMeta {
        private String mineType;
        private String endName;
        private Integer maxSize;
        private Boolean isImage;

        public FileMeta(){

        }

        public FileMeta(String mineType, String endName, Boolean isImage){
            this.mineType = mineType;
            this.endName = endName;
            this.isImage = isImage;
            this.maxSize = maxSize5M;
        }

        public FileMeta(String mineType, String endName, Boolean isImage, Integer maxSize){
            this.mineType = mineType;
            this.endName = endName;
            this.isImage = isImage;
            this.maxSize = maxSize;
        }

        public String getMineType() {
            return mineType;
        }
        public void setMineType(String mineType) {
            this.mineType = mineType;
        }
        public String getEndName() {
            return endName;
        }
        public void setEndName(String endName) {
            this.endName = endName;
        }
        public Integer getMaxSize() {
            return maxSize;
        }
        public void setMaxSize(Integer maxSize) {
            this.maxSize = maxSize;
        }

        public Boolean getIsImage() {
            return isImage;
        }

        public void setIsImage(Boolean isImage) {
            this.isImage = isImage;
        }


    }

    public static List<FileMeta> allowedFileType = null;

    {   // 初始化允许的文件类型
        allowedFileType = new ArrayList<FileMeta>();
        allowedFileType.add(new FileMeta("application/pdf", ".pdf", false, maxSize10M));
        allowedFileType.add(new FileMeta("application/x-rar-compressed", ".rar", false, maxSize10M));
        allowedFileType.add(new FileMeta("application/zip", ".zip", false, maxSize10M));
        allowedFileType.add(new FileMeta("application/vnd.ms-powerpoint", ".ppt", false, maxSize10M));
        allowedFileType.add(new FileMeta("application/vnd.openxmlformats-officedocument.presentationml.pptx", ".pdf", false, maxSize10M));
        allowedFileType.add(new FileMeta("application/msword", ".doc", false, maxSize10M));
        allowedFileType.add(new FileMeta("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx", false, maxSize10M));
        allowedFileType.add(new FileMeta("application/vnd.ms-excel", ".xls", false, maxSize10M));
        allowedFileType.add(new FileMeta("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx", false, maxSize10M));
        allowedFileType.add(new FileMeta("application/x-shockwave-flash", ".swf", false, maxSize10M));
        allowedFileType.add(new FileMeta("video/x-flv", ".flv", false, maxSize10M));
        allowedFileType.add(new FileMeta("video/mp4", ".mp4", false, maxSize10M));
        allowedFileType.add(new FileMeta("video/mpeg", ".mpeg", false, maxSize10M));
        allowedFileType.add(new FileMeta("image/png", ".png", true));
        allowedFileType.add(new FileMeta("image/x-png", ".png", true));
        allowedFileType.add(new FileMeta("image/pjpeg", ".jpg", true));
        allowedFileType.add(new FileMeta("image/jpeg", ".jpg", true));
        allowedFileType.add(new FileMeta("image/gif", ".gif", true));
        allowedFileType.add(new FileMeta("image/bmp", ".bmp", true));
    }

    /**
     * 文件的存储位置
     */
    @Value("#{configProperties['fileUpload.fileRootDir']}")
    private String fileRootDir;

    /**
     * 文件的访问链接
     */
    @Value("#{configProperties['fileUpload.fileUrl']}")
    private String fileUrl;

    @Resource
    FileService fileService;

    @Authorization
    @RequestMapping(value = "/management", method = RequestMethod.POST)
    public @ResponseBody BaseModel<FileResources> uploadFile(
            @RequestParam(value = "uploadFile")MultipartFile uploadFile,
            HttpServletRequest request) {

        BaseModel<FileResources> result = new BaseModel<>();

        String url = ""; // 文件的访问链接

        String fileName = ""; // 文件名

        String type = ""; // 文件类型


        try {

            type = uploadFile.getContentType(); // 获取文件类型

            String endName = ""; // 后缀名

            Integer maxSize = 0;

            boolean fileAllowed = false;

            for(FileMeta fileMeta : allowedFileType) { // 判断文件大小与类型是否适配
                if(type != null && fileMeta.getMineType().equals(type)) {
                    endName = fileMeta.getEndName();
                    maxSize = fileMeta.getMaxSize();
                    fileAllowed = true;
                    break;
                }
            }

            if(!fileAllowed) {
                result.setStatus(Constants.FAIL_BUSINESS_ERROR);
                result.setMessage("不支持的文件格式");
                return result;
            }
            else {
                Long fileSize = uploadFile.getSize();

                if(fileSize > maxSize) {
                    result.setStatus(Constants.FAIL_BUSINESS_ERROR);
                    result.setMessage("文件过大，图片最大为5M，非图片10M");
                    return result;
                }

                Long currentTime = System.currentTimeMillis();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                String fileRelativeDir = year + File.separator + month + File.separator + day + File.separator; // 文件的日期格式路径

                fileName = currentTime + endName; // 文件名

                logger.info("文件名为: " + fileName);

                File pFile = new File(fileRootDir+ fileRelativeDir + fileName);

                if (!pFile.exists()) {
                    pFile.mkdirs();
                }

                uploadFile.transferTo(pFile);

                url = fileUrl + fileRelativeDir + fileName;

                FileResources fileResources = new FileResources();

                fileResources.setFileName(fileName);
                fileResources.setCreateBy(1);
                fileResources.setCreateTime(currentTime);
                fileResources.setUpdateBy(1);
                fileResources.setUpdateTime(currentTime);
                fileResources.setSize(String.valueOf(fileSize));
                fileResources.setType(type);
                fileResources.setUrl(url);

                FileResources fileResources1 = fileService.addNewFile(fileResources);

                result.setData(fileResources1);

            }

        }
        catch (Exception e) {
            e.printStackTrace();
            result.setMessage("出现错误：" + e.getMessage());
            result.setStatus(Constants.FAIL_BUSINESS_ERROR);
        }

        

        return result;
    }


}
