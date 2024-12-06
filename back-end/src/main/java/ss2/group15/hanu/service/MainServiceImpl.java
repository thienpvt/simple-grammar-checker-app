package ss2.group15.hanu.service;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import ss2.group15.hanu.common.PagingResponse;
import ss2.group15.hanu.common.ResponseFrontendDefine;
import ss2.group15.hanu.common.ResponseModel;
import ss2.group15.hanu.entity.Content;
import ss2.group15.hanu.repository.ContentRepository;

import java.time.LocalDateTime;


@Service
public class MainServiceImpl implements MainService {

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private ContentRepository repository;

    @Override
    public ResponseModel saveContent(Content content, HttpServletRequest request) {
        try{
            content.setSub(getSub(request));
            repository.save(content);
            return new ResponseModel(HttpStatus.SC_OK, ResponseFrontendDefine.CODE_SUCCESS, "Success", null);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(HttpStatus.SC_INTERNAL_SERVER_ERROR, ResponseFrontendDefine.CODE_FAIL, e.getMessage(), null);
        }
    }

    @Override
    public ResponseModel getAllContent(String keyword, String type, String dateFrom, String dateTo,int page, int size,HttpServletRequest request) {
        try{
            Pageable pageable = PageRequest.of(page, size);
            LocalDateTime dateFromLocal = dateFrom.isEmpty()?null:LocalDateTime.parse(dateFrom+"T00:00:00");
            LocalDateTime dateToLocal = dateTo.isEmpty()?null:LocalDateTime.parse(dateTo+"T23:59:59");
            Page<Content> contentList = repository.doSearch(keyword.isEmpty()?null:keyword,type.isEmpty()?null:type,dateFromLocal,dateToLocal,getSub(request), pageable);
            if (contentList == null || contentList.isEmpty()) {
                ResponseModel responseModel = new ResponseModel();
                responseModel.setMessage("Not found.");
                responseModel.setStatusCode(HttpStatus.SC_OK + "");
                responseModel.setCode(ResponseFrontendDefine.CODE_NOT_FOUND);
                return responseModel;
            }
            PagingResponse<Content> pagingResponse = new PagingResponse<>();
            pagingResponse.setItems(contentList.getContent());
            pagingResponse.setTotal(contentList.getTotalElements());
            return new ResponseModel(HttpStatus.SC_OK, ResponseFrontendDefine.CODE_SUCCESS, "Success", pagingResponse);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(HttpStatus.SC_INTERNAL_SERVER_ERROR, ResponseFrontendDefine.CODE_FAIL, e.getMessage(), null);
        }
    }

    private String getSub(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String jwtToken = authorizationHeader.substring(7);
        var jwt = jwtDecoder.decode(jwtToken);
        return jwt.getSubject();
    }
}
