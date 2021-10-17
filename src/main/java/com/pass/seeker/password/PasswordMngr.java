package com.pass.seeker.password;

import com.pass.seeker.exception.ConfigurationException;
import com.pass.seeker.util.FileUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static com.pass.seeker.constant.PasswordConstants.LUP_PATH;

public class PasswordMngr {



    public static Optional<String> readLastUsedPassword(){

        Path lup = Path.of(LUP_PATH);
        String readedPass = null;
        if(lup.toFile().exists()){
            List<String> lines = FileUtils.readFile(LUP_PATH);
            if(lines.size() > 0)
                readedPass = lines.get(0);
        }

        Character[] dic = DictionaryMngr.getCharacters();
        Optional.ofNullable(readedPass).ifPresent( pass ->{
                if(pass.chars().anyMatch(c -> !ArrayUtils.contains(dic,(char)c)))
                    throw new ConfigurationException("Password must contain chars from the dictionary");
        });

        return Optional.ofNullable(readedPass);
    }
}
