package com.deadpineapple.front.controllers;

import com.deadpineapple.dal.constante.Constante;
import com.deadpineapple.dal.dao.IConvertedFileDao;
import com.deadpineapple.dal.dao.ITransactionDao;
import com.deadpineapple.dal.dao.IUserDao;
import com.deadpineapple.dal.entity.ConvertedFile;
import com.deadpineapple.dal.entity.Transaction;
import com.deadpineapple.dal.entity.UserAccount;
import com.deadpineapple.front.Forms.LoginForm;
import com.deadpineapple.front.tools.PayPalService;
import com.deadpineapple.front.tools.VideoFile;
import com.deadpineapple.videoHelper.TimeSpan;
import com.deadpineapple.videoHelper.information.VideoInformation;
import com.dropbox.core.*;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.SearchResult;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

/**
 * Created by saziri on 14/03/2016.
 */
@SuppressWarnings("ALL")
@Controller
@RequestMapping("/upload")
public class UploadController extends HttpServlet {
    // Video variables
    @Autowired
    IConvertedFileDao convertedFileDao;
    ConvertedFile convertedFile;
    ArrayList<VideoFile> videoFiles = new ArrayList<VideoFile>();
    JSONArray json;

    // Transaction variables
    @Autowired
    ITransactionDao transactionDao;
    ArrayList<Transaction> invoice;

    @Autowired
    IUserDao userBdd;

    public void setUserDAO(IUserDao userDAO) {
        this.userBdd = userDAO;
    }

    String UPLOAD_PATH;
    LoginForm userData;
    UserAccount user;
    VideoInformation videoInformation;
    PayPalService ps;
    ArrayList<String> dropboxFolders;

    // Dropbox config
    final String APP_KEY = "3xt31on71g5n2d6";
    final String APP_SECRET = "01hgg9uje17vwwv";
    final String URL_SITE = "http://localhost:8080/upload/auth";
    DbxWebAuth webAuth;
    DbxClientV2 client;
    DbxRequestConfig config;
    JSONArray history;

    List<String> ext = new ArrayList<String>(Arrays.asList(Constante.AcceptedUploadedTypes));

    public void setConvertedFileDao(IConvertedFileDao convertedFileDao) {
        this.convertedFileDao = convertedFileDao;
    }

    public void setTransactionDao(ITransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String uploadPage(HttpServletRequest request, Model model, HttpServletResponse response, LoginForm loginForm) throws JsonReader.FileLoadException, IOException {
        userData = (LoginForm) request.getSession().getAttribute("LOGGEDIN_USER");
        user = (UserAccount) request.getSession().getAttribute("USER_INFORMATIONS");
        if (user == null) {
            // Create an user with ip identifiant
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            user = new UserAccount();
            TimeSpan date = new TimeSpan();
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }
            dropboxFolders = new ArrayList<String>();
            user.setEmail(ipAddress + date);
            user.setPassword(LoginForm.getEncryptedPassword(ipAddress + date));
            Date creationDate = new Date();
            user.setCreationDate(creationDate);
            userBdd.saveUser(user);
            request.getSession().setAttribute("USER_INFORMATIONS", user);
            model.addAttribute("loginAttribute", loginForm);
        }
        UPLOAD_PATH = request.getServletContext().getRealPath("/") + "upload/"
                + user.getId() + "/";

        // Initiate an instance of dropbox
        model.addAttribute("dropboxUrl", getDropBoxUrl(request));
        if (dropboxFolders != null) {
            model.addAttribute("dropboxFiles", dropboxFolders);
        }
        return "upload";
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void uploadVideo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new IllegalArgumentException("Request is not multipart, please 'multipart/form-data' enctype for your form.");
        }
        ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        json = new JSONArray();
        try {
            List<FileItem> items = uploadHandler.parseRequest(request);
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    new File(UPLOAD_PATH).mkdirs();
                    File file = new File(UPLOAD_PATH, item.getName());
                    file.createNewFile();
                    item.write(file);

                    // Save video in bdd
                    String filePath = UPLOAD_PATH + item.getName();
                    Date creationDate = new Date();
                    VideoFile video = new VideoFile();
                    convertedFile = new ConvertedFile();
                    convertedFile.setUserAccount(user);
                    convertedFile.setFilePath(filePath);
                    convertedFile.setCreationDate(creationDate);
                    convertedFile.setOriginalName(item.getName());
                    convertedFile.setOldType("." + FilenameUtils.getExtension(filePath));
                    convertedFile.setNewType(".avi");
                    // Convert in MB
                    double filesize = ((double) item.getSize() / 1024) / 1024;
                    filesize = Math.round(filesize * 100.0) / 100.0;
                    convertedFile.setSize(filesize);
                    convertedFileDao.createFile(convertedFile);

                    // Generate video Information for the uploaded file (ffmpeg)
                    videoInformation = new VideoInformation(convertedFile.getFilePath());
                    // Link the converted file with it's video information
                    video.setConvertedFile(convertedFile);
                    video.setVideoInformation(videoInformation);
                    video.setPrice(generatePrice(videoInformation.getDuration()));

                    //check if video exist in the array
                    boolean vidExist = false;
                    for (VideoFile vidFile : videoFiles) {
                        if (vidFile.getConvertedFile().getOriginalName().equals(video.getConvertedFile().getOriginalName()))
                            vidExist = true;
                    }

                    if (!vidExist)
                        videoFiles.add(video);

                    json.put(generateJsonForPrview(video));
                    System.out.println(json.toString());
                }
            }
        } catch (FileUploadException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            writer.write(json.toString());
            writer.close();
        }
    }

    // Return the videos already uploaded and non payed yet
    @RequestMapping(value = "/getFiles", method = RequestMethod.GET)
    public void getUploadedFiles(HttpServletResponse response) throws IOException {
        history = new JSONArray();
        List<ConvertedFile> cfs = convertedFileDao.findByUser(user);
        for (ConvertedFile cf : cfs) {
            if ( (cf.getConverted() == null || !cf.getConverted()) && (cf.getInConvertion() == null || !cf.getInConvertion()) && (cf.getFilePath() != null || cf.getFilePath() == "")) {
                // Generate video Information for the uploaded file (ffmpeg)
                videoInformation = new VideoInformation(cf.getFilePath());
                // Link the converted file with it's video information
                VideoFile video = new VideoFile();
                video.setVideoInformation(videoInformation);
                video.setConvertedFile(cf);
                // Save the price
                video.setPrice(generatePrice(videoInformation.getDuration()));
                //Save videos in converted files for the transaction later

                //check if video exist in the array
                boolean vidExist = false;
                for (VideoFile vidFile : videoFiles) {
                    if (vidFile.getConvertedFile().getOriginalName().equals(video.getConvertedFile().getOriginalName()))
                        vidExist = true;
                }

                if (!vidExist)
                    videoFiles.add(video);


                history.put(generateJsonForPrview(video));
            }
        }
        if (history != null) {
            PrintWriter writer = response.getWriter();
            response.setContentType("application/json");
            writer.write(history.toString());
            writer.close();
        } else {
            response.setStatus(200);
        }
    }

    // Generate an image for the uploaded video
    @RequestMapping(value = "/getThumb", method = RequestMethod.GET)
    public void getThumb(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getParameter("getthumb") != null && !request.getParameter("getthumb").isEmpty()) {
            String filePath = UPLOAD_PATH + request.getParameter("getthumb");
            String imageName = request.getParameter("getthumb");
            imageName = imageName.substring(0, imageName.lastIndexOf('.')) + ".png";
            String thumb = UPLOAD_PATH + "thumb_" + imageName;
            for (VideoFile video :
                    videoFiles) {
                if (video.getConvertedFile().getFilePath().equals(filePath)) {
                    videoInformation = video.getVideoInformation();
                    break;
                }
            }

            videoInformation.generateAThumbnailImage(thumb);
            File file = new File(thumb);
            BufferedImage resizeImagePng;
            if (!file.exists()) {
                thumb = "/resources/img/video-player.png";
                // Retrieve image from the classpath.
                InputStream is = this.getClass().getResourceAsStream(thumb);
                resizeImagePng = resizeImage(ImageIO.read(is));
                ImageIO.write(resizeImagePng, "png", file);
            } else {
                resizeImagePng = resizeImage(ImageIO.read(file));
            }

            ImageIO.write(resizeImagePng, "png", file);


            int bytes = 0;
            ServletOutputStream op = response.getOutputStream();

            response.setContentType("image/png");
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");

            byte[] bbuf = new byte[1024];
            DataInputStream in = new DataInputStream(new FileInputStream(file));

            while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
                op.write(bbuf, 0, bytes);
            }

            in.close();
            op.flush();
            op.close();

        }
    }

    @RequestMapping(value = "/deleteFile", method = RequestMethod.GET)
    public void deleteFile(HttpServletRequest request, HttpServletResponse resp, Model model) throws JsonReader.FileLoadException {
        if (request.getParameter("delfile") != null && !request.getParameter("delfile").isEmpty()) {
            File file = new File(UPLOAD_PATH + request.getParameter("delfile"));
            File thumb = new File(UPLOAD_PATH + "thumb_" + request.getParameter("delfile"));
            String filePath = UPLOAD_PATH + request.getParameter("delfile");
            java.nio.file.Path p = Paths.get(filePath);

            String fileName = p.getFileName().toString();
            List<ConvertedFile> cf = convertedFileDao.findByUser(user);
            for (ConvertedFile video :
                    cf) {
                if (video.getOriginalName().equals(fileName)) {
                    // delete file from bdd
                    convertedFileDao.deleteFile(video);
                    // delete file from user array
                    VideoFile.deleteVideoInformation(videoFiles, video);
                    // Delete file from
                    if (file.exists()) {
                        file.delete();
                    }
                    if (thumb.exists()) {
                        thumb.delete();
                    }
                    resp.setStatus(200);
                    return;
                }
            }
            resp.setStatus(404);
        }

    }

    @RequestMapping(value = "/setFormat", method = RequestMethod.GET)
    public void setConvertFormat(HttpServletRequest request, HttpServletResponse resp) {
        if (request.getParameter("format") != null && !request.getParameter("format").isEmpty()) {
            String filePath = UPLOAD_PATH + request.getParameter("file");
            String format = request.getParameter("format");
            List<ConvertedFile> cf = convertedFileDao.findByUser(user);
            for (ConvertedFile video :
                    cf) {
                if (video.getFilePath().equals(filePath)) {
                    System.out.println("Set format" + filePath);
                    video.setNewType(format);
                    convertedFileDao.updateFile(video);

                    resp.setStatus(200);
                    return;
                }
            }
            resp.setStatus(404);
        }
    }

    @RequestMapping(value = "/setEncodage", method = RequestMethod.GET)
    public void setConvertEncodate(HttpServletRequest request, HttpServletResponse resp) {
        if (request.getParameter("encodage") != null) {
            String filePath = UPLOAD_PATH + request.getParameter("file");
            String format = request.getParameter("encodage");
            List<ConvertedFile> cf = convertedFileDao.findByUser(user);
            for (ConvertedFile video :
                    cf) {
                if (video.getFilePath().equals(filePath)) {
                    System.out.println("Set Encodage" + filePath);
                    if (format.isEmpty()) {
                        video.setNewEncoding(null);
                    } else {
                        video.setNewEncoding(format);
                    }
                    convertedFileDao.updateFile(video);

                    resp.setStatus(200);
                    return;
                }
            }
            resp.setStatus(404);
        }
    }

    @RequestMapping(value = "/facture", method = RequestMethod.GET)
    public String convert(HttpServletRequest request) {
        // If user is not connected, redirect him to create account
        if (userData == null) {
            return "redirect:/user/add";
        }
        // IF there is no video uploaded, redirect user to upload page
        if (videoFiles.size() == 0) {
            return "redirect:/upload";
        }
        // Create the Transaction and redirect to PayPal

        Date transactionDate = new Date();
        Double priceTotal = 0.0;
        invoice = new ArrayList<Transaction>();
        int idTransation = transactionDao.getNextIdTransaction();
        for (VideoFile vf : videoFiles) {
            Transaction transaction = new Transaction();
            transaction.setConvertedFiles(vf.getConvertedFile());
            transaction.setPrix(vf.getPrice());
            transaction.setDate(transactionDate);
            transaction.setUserAccount(user);
            transaction.setPayed(false);
            transaction.setIdTransaction(idTransation);
            invoice.add(transactionDao.createTransaction(transaction));
            priceTotal += vf.getPrice();
        }
        priceTotal = Math.round(priceTotal * 100.0) / 100.0;
        //test price : 0.10
        ps = new PayPalService(priceTotal);
        String serverUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        // create the paypal payment
        String paypalURL = ps.startCheckOut(serverUrl);

        // redirect to paypal
        // You can use this test account :
        //   login : testBuyer@test.co
        //   passd : Pa$$w0rd1
        if (paypalURL != null)
            return "redirect:" + paypalURL;
        else
            return "";
    }

    @RequestMapping(value = "/payment", method = RequestMethod.GET)
    public String finish(HttpServletRequest request) {
        // retrieve token and stuff
        String paymentID = (String) request.getParameter("paymentId");
        String token = (String) request.getParameter("token");
        String payerID = (String) request.getParameter("PayerID");

        // execute the payment
        if (paymentID != null && token != null && payerID != null) {
            boolean transactStatus = ps.finishCheckOut(paymentID, payerID, token);
            if (transactStatus) {
                setInvoicePayed(invoice);
                return "redirect:/dashboard";
            } else
                return "redirect:/upload";
        }
        return "redirect:/upload";
    }


    private static BufferedImage resizeImage(BufferedImage originalImage) {
        BufferedImage resizedImage = new BufferedImage(80, 57, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, 80, 57, null);
        g.dispose();

        return resizedImage;
    }

    private String getDropBoxUrl(HttpServletRequest request) throws JsonReader.FileLoadException {
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        config = new DbxRequestConfig(
                "JavaTutorial/1.0", Locale.getDefault().toString());
        HttpSession session = request.getSession(true);
        String sessionKey = "dropbox-auth-csrf-token";
        DbxSessionStore csrfTokenStore = new DbxStandardSessionStore(session, sessionKey);
        webAuth = new DbxWebAuth(config, appInfo, URL_SITE, csrfTokenStore);
        String authorizeUrl = webAuth.start();
        return authorizeUrl;
    }

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String auth(HttpServletRequest request, HttpServletResponse response, Model model) throws DbxException, IOException {
        // Load the request token we saved in part 1.
        DbxAuthFinish authFinish = null;
        try {
            authFinish = webAuth.finish(request.getParameterMap());
        } catch (DbxWebAuth.BadRequestException ex) {
            log("On /dropbox-auth-finish: Bad request: " + ex.getMessage());
            response.sendError(400);
        } catch (DbxWebAuth.BadStateException ex) {
            // Send them back to the start of the auth flow.
            response.sendRedirect("http://my-server.com/dropbox-auth-start");
        } catch (DbxWebAuth.CsrfException ex) {
            log("On /dropbox-auth-finish: CSRF mismatch: " + ex.getMessage());
        } catch (DbxWebAuth.NotApprovedException ex) {
            // When Dropbox asked "Do you want to allow this app to access your
            // Dropbox account?", the user clicked "No".
        } catch (DbxWebAuth.ProviderException ex) {
            System.out.println("On /dropbox-auth-finish: Auth failed: " + ex.getMessage());
            response.sendError(503, "Error communicating with Dropbox.");
        } catch (DbxException ex) {
            System.out.println("On /dropbox-auth-finish: Error getting token: " + ex.getMessage());
            response.sendError(503, "Error communicating with Dropbox.");
        }

        if (authFinish != null) {
            String accessToken = authFinish.getAccessToken();
            client = new DbxClientV2(config, accessToken);
            //System.out.println("Linked account: " + client.getAccountInfo().displayName);
            dropboxFolders = getVideoFiles();
        }
        return "redirect:/upload";
    }

    private ArrayList<String> getVideoFiles() throws DbxException {
        ArrayList<String> dropboxFolders = new ArrayList();
        try {
            for (String format : ext) {
                SearchResult search = client.files().search("", format);
                for (int i = 0; i < search.getMatches().size(); i++) {
                    String pat = search.getMatches().get(i).getMetadata().getPathDisplay();
                    dropboxFolders.add(pat);
                    System.out.println(pat);
                }
            }
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return dropboxFolders;
    }

    @RequestMapping(value = "/uploadDb", method = RequestMethod.GET)
    public void downloadDropboxFile(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String dbFilePath = request.getParameter("fileName");
        if (dbFilePath == null || dbFilePath.equals("")) {
            throw new ServletException("File Name can't be null or empty");
        }
        JSONArray t = new JSONArray();
        java.nio.file.Path p = Paths.get(dbFilePath);
        String fileName = p.getFileName().toString();
        String serverPath = UPLOAD_PATH + fileName;
        double size;
        FileOutputStream outputStream = new FileOutputStream(serverPath);
        try {
            DbxDownloader<FileMetadata> download = null;
            try {
                download = client.files().download(dbFilePath);
            } catch (DbxException e) {
                System.out.println("Can't find the file db");
                e.printStackTrace();
            }
            try {
                download.download(outputStream);
            } catch (DbxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            size = download.getResult().getSize() / 10000;
            size = size / 100;

        } finally {
            try {
                outputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        //Save video in bdd

        Date creationDate = new Date();
        VideoFile video = new VideoFile();
        convertedFile = new ConvertedFile();
        convertedFile.setUserAccount(user);
        convertedFile.setFilePath(serverPath);
        convertedFile.setCreationDate(creationDate);
        convertedFile.setOriginalName(fileName);
        convertedFile.setOldType("." + FilenameUtils.getExtension(dbFilePath));
        convertedFile.setNewType(".avi");

        convertedFile.setSize(size);
        convertedFileDao.createFile(convertedFile);

        // Create a new video Information
        videoInformation = new VideoInformation(serverPath);
        String imageName = fileName.substring(0, fileName.lastIndexOf('.')) + ".png";
        String thumb = UPLOAD_PATH + "thumb_" + imageName;
        videoInformation.generateAThumbnailImage(thumb);

        // add video information into converted file
        video.setConvertedFile(convertedFile);
        video.setVideoInformation(videoInformation);
        video.setPrice(generatePrice(videoInformation.getDuration()));
        videoFiles.add(video);
        response.setContentType("application/json");
        t.put(generateJsonForPrview(video));
        writer.write(t.toString());
        writer.close();
    }

    public JSONObject generateJsonForPrview(VideoFile video) {
        JSONObject jsonFile = new JSONObject();
        String fileName = video.getConvertedFile().getOriginalName();
        double size = video.getConvertedFile().getSize();
        TimeSpan duration = video.getVideoInformation().getDuration();
        jsonFile.put("name", fileName);
        jsonFile.put("size", size);
        jsonFile.put("duration", duration);
        String aPrice = String.format("%.2f", generatePrice(duration));
        aPrice = aPrice.replace(",", ".");
        jsonFile.put("price", aPrice);
        jsonFile.put("url", "UploadServlet?getfile=" + fileName);
        jsonFile.put("thumbnail_url", "/upload/getThumb?getthumb=" + fileName);
        jsonFile.put("delete_url", "/upload/deleteFile?delfile=" + fileName);
        jsonFile.put("delete_type", "GET");
        return jsonFile;
    }

    public double generatePrice(TimeSpan duration) {
        double price = 0, time = 0;
        if (duration != null) {
            time = (double) ((duration.getHeures() * 60) + duration.getMinutes());
            System.out.println("temps =" + time + duration.getHeures() * 60);
        }

        if (time < 5) {
            time = 5;
        }
        price = (Math.log(time) - 1) / 3;
        price = Math.round(price * 100.0) / 100.0;
        return price;
    }

    private void setInvoicePayed(ArrayList<Transaction> invoice) {
        for (Transaction transaction : invoice) {
            transaction.setPayed(true);
            transactionDao.createTransaction(transaction);
        }
    }


}
