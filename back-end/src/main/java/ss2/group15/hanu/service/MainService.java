package ss2.group15.hanu.service;

import jakarta.servlet.http.HttpServletRequest;
import ss2.group15.hanu.common.ResponseModel;
import ss2.group15.hanu.entity.Content;

import java.security.Principal;

public interface MainService {

    ResponseModel saveContent(Content content, HttpServletRequest request);

    ResponseModel getAllContent(String keyword, String type, String dateFrom, String dateTo,int page, int size,HttpServletRequest request);
}
