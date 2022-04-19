package life.majiang.community.controller;

import life.majiang.community.dto.FileDTO;
import life.majiang.community.model.User;
import life.majiang.community.provider.UFileResult;
import life.majiang.community.provider.UFileService;
import life.majiang.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by codedrinker on 2019/6/26.
 */
@Controller
@Slf4j
public class FileController {
    @Autowired
    private UFileService uFileService;

    @Autowired
    private UserService userService;


    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        try {
            UFileResult uFileResult = uFileService.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(uFileResult.getFileUrl());

            return fileDTO;
        } catch (Exception e) {
            log.error("upload error", e);
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(0);
            fileDTO.setMessage("上传失败");
            return fileDTO;
        }
    }

    @RequestMapping("/file/uploadhead")
    public String uploadhead(HttpServletRequest request,
                             Model model) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        try {
            UFileResult uFileResult = uFileService.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(1);
            fileDTO.setUrl(uFileResult.getFileUrl());
            User user = (User) request.getSession().getAttribute("user");
            user.setAvatarUrl(uFileResult.getFileUrl());
            userService.updateAvatarUrl(user);
            User user1 = new User();
            user1=userService.getUsername(user.getAccountId());
            model.addAttribute("myusername", user1.getAccountId());
            model.addAttribute("myemail", user1.getEmail());
            model.addAttribute("myname", user1.getName());
            model.addAttribute("info", user1.getBio());
            model.addAttribute("myavl",user.getAvatarUrl());


            return "myInfo";
        } catch (Exception e) {
            log.error("upload error", e);
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSuccess(0);
            fileDTO.setMessage("上传失败");
            return "myInfo";
        }
    }
}
