/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.axle.chassis.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.Id;
//import ke.co.tracom.gulf.dcc.entities.CmsUser;
//import ke.co.tracom.gulf.dcc.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

/**
 * Used to handle common functions. For example;
 * <ul>
 * <li>Parse javax errors in a {@link List}</li>
 * <li>Get system date in future to enforce locale</li>
 * <li>Write {@link MultipartFile} to file system</li>
 * <li>Get entity name for future release we will be able to get the database name</li>
 * <li>Encrypt and decrypt text using a default key</li>
 * <li>Retrieve the ID of an entity</li>
 * <li>Get a {@link List} of generic parameters of a generic class</li>
 * </ul>
 * @author Cornelius M
 * @version 0.0.1
 * @author Owori Juma
 * @version 1.2.3
 */
@Component
public class SharedMethods {

    /**
     * Used to handle logging
     */
    private final Logger log = LoggerFactory.getLogger(this.getClass());  
    /**
     * Used to format date
     */
    private final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    /**
     * Encryption key
     */
    private static final  String key = "Trx#279!DTCeioc?";

    /**
     * Used to get all errors each mapped to the relevant field. Quiet handy
     * when returning API error responses
     *
     * @param validation
     * @return a {@link Map} of all errors
     */
    public static Map<String, String> getFieldMapErrors(BindingResult validation) {
        return validation.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }
    
    /**
     * Converts errors into a string
     * @param error
     * @return {@link String} of errors
     */
    public static String getFieldErrorsString(Errors error) {
        String eString = "";
        eString = error.getFieldErrors().stream().map((fError) -> fError.getField() + " => " + fError.getDefaultMessage() + ", ").reduce(eString, String::concat);
        return eString;
    }

    /**
     * Used to fetch current date. At a later stage it may be to a relevant
     * timezone
     *
     * @return current date
     */
    public static Date currentDate() {
        return new Date();
    }

    /**
     * Used to Read MultipartFile resource e.g a CSV file
     *
     * @param resource MultipartFile resource to be read
     * @param uploadPath
     * @throws FileNotFoundException, IOException when errors occur searching
     * for the file or reading the file
     * @return
     * @throws IOException
     */
    public String store(MultipartFile resource, String uploadPath) throws FileNotFoundException, IOException {
        log.info("Processing file storage for file " + resource.getOriginalFilename());

        String fileUrl = uploadPath + sdf.format(new Date()) + " " + resource.getOriginalFilename();
        BufferedOutputStream stream
                = new BufferedOutputStream(new FileOutputStream(new File(fileUrl)));
        stream.write(resource.getBytes());
        stream.close();
        return fileUrl;
    }

    /**
     * Used to write multipart file in syste file
     *
     * @param resource
     * @param uploadPath
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public File storeFile(MultipartFile resource, String uploadPath) throws FileNotFoundException, IOException {
        log.info("Processing file storage for file " + resource.getOriginalFilename());
        String fileUrl = uploadPath + sdf.format(new Date()) + " " + resource.getOriginalFilename();
        File file = new File(fileUrl);
        log.debug("Initialized new file space {} ", file.getTotalSpace());
        BufferedOutputStream stream
                = new BufferedOutputStream(new FileOutputStream(file));
        stream.write(resource.getBytes());
        stream.close();
        log.debug("Saved new file in system directory space {} ", file.getTotalSpace());
        return file;
    }

    /**
     * Used to split string by camel case and convert it to title case e.g.
     * "camelCase" will be "Camel Case"
     *
     * @param camelString
     * @return
     */
    public static String splitCamelString(String camelString) {
        return StringUtils.capitalize(StringUtils.join(
                camelString.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"), ' '));
    }

    /**
     * Split car registration number e.i KBC345K to KBC 345K
     *
     * @param regNumber
     * @return 
     */
    public static String splitCarReg(String regNumber) {
        String refString;
        int nIndex = 0;
        for (char c : regNumber.toCharArray()) {//search first number index
            if (Character.isDigit(c)) {
                break;
            }
            nIndex++;
        }
        refString = regNumber.substring(0, nIndex) + " " + regNumber.substring(nIndex);
        return refString;
    }

    /**
     * Get entity name
     *
     * @param <T> 
     * @param entity
     * @return entity name as a {@link String}
     */
    public static <T> String getEntityName(Class<T> entity) {
//        Table table = entity.getAnnotation(Table.class);
//        return table.name();
        return entity.getSimpleName();
    }

    /**
     * Encrypt text
     *
     * @param text 
     * @return base64 encoded encrypted {@link String} or the un encrypted text if an error occurs
     */
    public static String encryptText(String text) {
        try {
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
//            return new String(encrypted);
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NullPointerException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
            java.util.logging.Logger.getLogger(SharedMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
        return text;
    }

    /**
     * Decrypt text
     *
     * @param encryptedText base64 encoded
     * @return the decrypted text or encrypted text if an error occurs
     */
    public static String decryptText(String encryptedText) {

        try {
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKey);
//            return new String(cipher.doFinal(encryptedText.getBytes()));
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
        } catch (IllegalArgumentException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NullPointerException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
            java.util.logging.Logger.getLogger(SharedMethods.class.getName()).log(Level.SEVERE, null, ex);
        }
        return encryptedText;
    }
    
    /**
     * Get the id value of an entity
     *
     * @param entity 
     * @return id value
     */
    public static Object getEntityIdValue(Object entity) {
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return PropertyAccessorFactory.forBeanPropertyAccess(entity).getPropertyValue(field.getName());
            }
        }
        return null;
    }
    
    /**
     * Used to get generic classes declared by a template
     *
     * @param clazz the generic class
     * @return a {@link List} of {@link Class}(s)
     */
    public static List<Class> getGenericClasses(Class clazz) {
        List<Class> classes = new ArrayList<>();
        ParameterizedType t = (ParameterizedType) clazz.getGenericSuperclass();
        for (Type type : t.getActualTypeArguments()) {
            classes.add((Class<?>) type);
        }
        return classes;
    }

}
