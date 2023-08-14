package com.aoz.aozmeeting.service;

import com.aoz.aozmeeting.POJO.Department;
import com.aoz.aozmeeting.common.R;
import com.baomidou.mybatisplus.extension.service.IService;
import im.youdu.sdk.exception.AESCryptoException;
import im.youdu.sdk.exception.HttpRequestException;
import im.youdu.sdk.exception.ParamParserException;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * Create by LiuYang on 2023/5/20 22:18
 */
public interface DepartmentService extends IService<Department> {

    R<List<Department>> updateAllDepartment() throws ParamParserException, HttpRequestException, AESCryptoException;
}
