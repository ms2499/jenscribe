package com.jenscribe.demo.Controller;

import com.jenscribe.demo.Model.XmlElement;
import com.jenscribe.demo.Service.EnscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController {
    @Autowired
    EnscribeService ensService;

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello 測試";
    }

    @GetMapping("index.html")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/getData")
    @ResponseBody
    public List<String> getData(HttpServletResponse response, @RequestParam String fileName, @RequestParam int rows){
        System.out.println("start get data, file is "+fileName);
        List<String> data = ensService.getData(fileName, rows);
        if (data == null){
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return null;
        }else{
            try {
//                return new String(data.getBytes(StandardCharsets.ISO_8859_1), "BIG5");
                return data;
            }catch (Exception e){
                e.printStackTrace();
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                return null;
            }
        }
    }

    @GetMapping("/getXml")
    @ResponseBody
    public List<XmlElement> getXml(HttpServletResponse response, @RequestParam String fileName){
        return ensService.getXml(fileName);
    }
}
