package com.udemy.photoapp.utils;

import com.udemy.photoapp.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailConstructor {

    @Autowired
    private Environment environment;

    @Autowired
    private TemplateEngine templateEngine;


    public MimeMessagePreparator constructNewUserEmail(AppUser appUser, String password) {
        Context context = new Context();
        context.setVariable("user", appUser);
        context.setVariable("password", password);
        String text = templateEngine.process("newUserEmailTemplate", context);

        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                email.setPriority(1);
                email.setTo(appUser.getEmail());
                email.setSubject("Welcome");
                email.setText(text, true);
                email.setFrom(new InternetAddress(environment.getProperty("support.email")));
            }
        };
        return messagePreparator;
    }

    public MimeMessagePreparator constructResetPasswordEmail(AppUser user, String password) {
        Context context = new Context();
        context.setVariable("user", user);
        context.setVariable("password", password);
        String text = templateEngine.process("resetPasswordEmailTemplate", context);
        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                email.setPriority(1);
                email.setTo(user.getEmail());
                email.setSubject("New Password");
                email.setText(text, true);
                email.setFrom(new InternetAddress(environment.getProperty("support.email")));
            }
        };
        return messagePreparator;
    }


    public MimeMessagePreparator constructUpdateUserProfileEmail(AppUser user) {
        Context context = new Context();
        context.setVariable("user", user);
        String text = templateEngine.process("updateUserProfileEmailTemplate", context);
        MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
                email.setPriority(1);
                email.setTo(user.getEmail());
                email.setSubject("Profile Update");
                email.setText(text, true);
                email.setFrom(new InternetAddress(environment.getProperty("support.email")));
            }
        };
        return messagePreparator;
    }


}
