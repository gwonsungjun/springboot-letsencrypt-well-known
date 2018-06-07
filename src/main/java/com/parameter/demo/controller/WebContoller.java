package com.parameter.demo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Controller
@AllArgsConstructor
@Slf4j
public class WebContoller {

    private static final String PATH = "/templates/";

    @GetMapping("/")
    public String main(){
        return "test";
    }

    @GetMapping("/.well-known/acme-challenge/{id}")
    public void paramCheck(@PathVariable String id, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String uri = request.getRequestURI();
        log.info(">>>>>"+uri);

        String wellKnown = "/.well-known/acme-challenge/";

        if (!uri.startsWith(wellKnown)){
            response.sendError(404);
            return;
        }

        String param = id;
        String res = PATH+param;

        try{
            String respString = readResource(res);
            response.setContentType("text/plain");
            response.getOutputStream().print(respString.trim());
        }catch (Throwable e){
            log.info(">>>>>"+"not found");
            response.sendError(404);
        }
    }

    private String readResource(String resName) throws IOException {
        log.info(">>>>>"+"reading " + resName);
        InputStream in = getClass().getResourceAsStream(resName);
        return inputStreamToString(in);
    }

    private static String inputStreamToString(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}
