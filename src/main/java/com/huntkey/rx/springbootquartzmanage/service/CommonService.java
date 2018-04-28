package com.huntkey.rx.springbootquartzmanage.service;


/**
 * 通用Service接口
 *  生成单据单号 和文件上传 以及 未来自定义通用服务
 * @author zhangyu
 * @create 2018-01-02 11:49
 **/
public interface CommonService {

    /**
     * fastDFS文件上传
     *
     * @param groupName
     * @param inputStream
     * @param fileSize
     * @param fileExtName
     * @return
     */
//    StorePath fastDFSUpload(String groupName, InputStream inputStream, long fileSize, String fileExtName);
    
    /**
     * 获取编号(职位编码，单据编号)
     *
     * @param nbrlCode
     * @return
     */
    String getCode(String nbrlCode);

}
