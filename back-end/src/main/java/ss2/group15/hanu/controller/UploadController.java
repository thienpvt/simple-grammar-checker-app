package ss2.group15.hanu.controller;


import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ss2.group15.hanu.common.ResponseFrontendDefine;
import ss2.group15.hanu.common.ResponseModel;

@RestController("/upload")
public class UploadController {


    @PostMapping()
    public ResponseModel saveContent(@RequestBody MultipartFile file) {
        try{
            System.out.println("Upload file: " + file.getSize());
            return new ResponseModel(HttpStatus.SC_OK, ResponseFrontendDefine.CODE_SUCCESS, String.valueOf(file.getSize()), null);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(HttpStatus.SC_INTERNAL_SERVER_ERROR, ResponseFrontendDefine.CODE_FAIL, e.getMessage(), null);
        }
    }
}
