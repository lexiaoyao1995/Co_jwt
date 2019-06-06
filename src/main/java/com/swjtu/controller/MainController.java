package com.swjtu.controller;

import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.Context;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.swjtu.common.Info;
import com.swjtu.common.Result;
import com.swjtu.model.History;
import com.swjtu.model.ResultMap;
import com.swjtu.service.HistoryService;
import com.swjtu.service.UserService;
import com.swjtu.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
public class MainController implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    private Assistant service = null;
    private Context context = null;
    private ResultMap resultMap;
    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserService userService;

    @GetMapping("/ans")
    public Result ans(@RequestParam("node") String dialogNode) {
        InputData input = new InputData.Builder(dialogNode).build();
        MessageOptions options = new MessageOptions.Builder(Info.workspaceId).input(input).context(context).build();
        MessageResponse response = service.message(options).execute();
        context = response.getContext();
        List<String> answer = response.getOutput().getText();
        historyService.insert(new History(dialogNode,answer.get(0)));
        return Result.ok(response);
    }

    @PostMapping("login")
    public Result login(String username, String password) {
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            return Result.ok(JWTUtil.createToken(username));
        } catch (UnknownAccountException e) {
            return Result.build(403, "error username");
        } catch (IncorrectCredentialsException e) {
            return Result.build(403, "error password");
        }
    }

    @GetMapping("toLogin")
    public Result toLogin() {
        return Result.build(403, "need login");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("init");
        IamOptions iamOptions = new IamOptions.Builder().apiKey(Info.apikey).build();
        service = new Assistant("2018-08-30", iamOptions);
        service.setEndPoint(Info.Url);
    }

    @GetMapping("getHistory")
    public Result getHistory() {
        List<History> list = historyService.getAll();
        return Result.ok(list);
    }

    @PostMapping("insertHistory")
    public Result insertHistory(@RequestBody History history) {
        historyService.insert(history);
        return Result.ok();
    }

    @PostMapping("register")
    public Result register(String username, String password) {
        boolean b = userService.insert(username, password);
        return b ? Result.ok() : Result.build(500, "error");
    }

    @RequestMapping(path = "/unauthorized/{message}")
    public ResultMap unauthorized(@PathVariable String message) throws UnsupportedEncodingException {
        return resultMap.success().code(401).message(message);
    }

//	@Override
//	public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
//		return o;
//	}
//
//	@Override
//	public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
//		log.info("init");
//		IamOptions iamOptions = new IamOptions.Builder().apiKey(Info.apikey).build();
//		service = new Assistant("2018-08-30", iamOptions);
//		service.setEndPoint(Info.Url);
//		return o;
//	}
}
