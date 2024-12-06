package ss2.group15.hanu.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ss2.group15.hanu.common.ResponseModel;
import ss2.group15.hanu.entity.Content;
import ss2.group15.hanu.service.MainService;

import java.security.Principal;

/**
 * @author Thienpv
 * @Email pvtcwd@gmail.com
 * @Version 1.0
 * 12/3/2024
 * This class is example for a rest controller
 */

@RestController
@RequestMapping("/")
public class MainController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private MainService mainService;

    @PostMapping("/save")
    public ResponseModel saveContent(@RequestBody Content content) {
        return mainService.saveContent(content, request);
    }

    @GetMapping("/get-all")
    public ResponseModel getAllCategories(@RequestParam(value = "type", defaultValue = "") String type,
                                          @RequestParam(value = "keyword", defaultValue = "") String keyword,
                                          @RequestParam("dateFrom") String dateFrom,
                                          @RequestParam("dateTo") String dateTo,
                                          @RequestParam(value = "page",defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        return mainService.getAllContent(keyword, type, dateFrom, dateTo, page, size, request);
    }
}
