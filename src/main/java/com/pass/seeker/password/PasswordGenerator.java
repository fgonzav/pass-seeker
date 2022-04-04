package com.pass.seeker.password;

import com.pass.seeker.configuration.PropertiesFactory;
import com.pass.seeker.exception.ConfigurationException;
import com.pass.seeker.password.factory.PasswordRepositoryFactory;
import com.pass.seeker.password.repository.PasswordRepository;
import lombok.Getter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class PasswordGenerator {

    private static final Character[] characters = DictionaryMngr.getCharacters();

    @Getter
    private volatile String lastUsed;

    private final AtomicInteger lastUsedLength = new AtomicInteger(1);

    private static volatile PasswordGenerator passwordGenerator;

    private PasswordGenerator(){
        PasswordRepository repository = PasswordRepositoryFactory.getInstance();
        repository.getLastUsed().ifPresent(p -> this.lastUsed = p);
    }

    public static PasswordGenerator getInstance(){
        if(passwordGenerator == null){
            synchronized (PasswordGenerator.class){
                if(passwordGenerator == null){
                    passwordGenerator = new PasswordGenerator();
                }
            }
        }
        return passwordGenerator;
    }

    public synchronized Optional<String> getNext(){
        Optional<String> pwd = generate();
        pwd.ifPresent(this::updateState);
        return pwd;
    }

    private Optional<String> generate(){
        if (hasNext()){
            Character next = getNextCharacter(lastUsed);
            if(StringUtils.isEmpty(lastUsed)){
                return Optional.of(String.valueOf(next).repeat(lastUsedLength.get()));
            } else {
                for(int i = lastUsed.length()-1 ;i >= 0 ; i--){
                    char toReplace = lastUsed.charAt(i);
                    if(toReplace != characters[characters.length-1]){
                        String base = lastUsed.substring(0,i)+getNextCharacter(toReplace);
                        return Optional.of(base+characters[0].toString().repeat(lastUsedLength.get()-base.length()));
                    }
                }
            }
        }
        return Optional.empty();
    }

    private void updateState(String generatedPassword){
        if(String.valueOf(characters[characters.length-1]).repeat(lastUsedLength.get()).equals(generatedPassword)){
            lastUsedLength.addAndGet(1);
            lastUsed = null;
        } else {
            lastUsed = generatedPassword;
        }
    }

    private boolean hasNext(){
        return lastUsedLength.get() <= getPasswordMaxLength();
    }

    private Integer getPasswordMaxLength(){
        int userMaxLength = Integer.parseInt(StringUtils.defaultString(System.getProperty("maxLength"),"0"));
        if(userMaxLength < 0)
            throw new ConfigurationException("The max length must be higher than 0");

        Properties application = PropertiesFactory.getProperties("application");
        return userMaxLength == 0 ? Integer.parseInt((String) application.get("default.pass.max.length")): userMaxLength;

    }

    private static Optional<Character> getLastUsedChar(String lastUsed){
        if(StringUtils.isEmpty(lastUsed))
            return Optional.empty();

        return Optional.of(lastUsed.charAt(lastUsed.length()-1));
    }

    private Character getNextCharacter(Character ch){
        return getNextCharacter(ch.toString());
    }

    private Character getNextCharacter(String last){
        return Optional.ofNullable(last)
                .map(PasswordGenerator::mapToNextChar)
                .orElseGet(()->characters[0]);
    }

    private static Character mapToNextChar(String lastUsed){
        Optional<Character> opLastChar = getLastUsedChar(lastUsed);
        if(opLastChar.isEmpty())
            return characters[0];

        Character last = opLastChar.get();
        int i = ArrayUtils.indexOf(characters,last)+1;
        return i < characters.length? characters[i]:characters[0];
    }

    public void setLastUsed(String lastUsed){
        if(this.lastUsed == null) {
            synchronized (PasswordGenerator.class) {
                if (this.lastUsed == null) {
                    this.lastUsed = lastUsed;
                    this.lastUsedLength.set(lastUsed.length());
                }
            }
        }
    }
}
