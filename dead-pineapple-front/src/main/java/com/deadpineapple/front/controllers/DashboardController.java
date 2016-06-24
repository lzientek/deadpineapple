package com.deadpineapple.front.controllers;

import com.deadpineapple.dal.RabbitMqEntities.FileIsUploaded;
import com.deadpineapple.dal.dao.IConvertedFileDao;
import com.deadpineapple.dal.dao.ITransactionDao;
import com.deadpineapple.dal.entity.ConvertedFile;
import com.deadpineapple.dal.entity.Transaction;
import com.deadpineapple.dal.entity.UserAccount;
import com.deadpineapple.front.Forms.LoginForm;
import com.deadpineapple.front.tools.Invoice;
import com.deadpineapple.rabbitmq.RabbitInit;
import com.dropbox.core.*;
import com.dropbox.core.json.JsonReader;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.lowagie.text.Document;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.portlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by saziri on 16/05/2016.
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    LoginForm userData;
    UserAccount user;
    String DOWNLOAD_PATH;
    String INVOICE_PATH;

    // Transaction
    @Autowired
    ITransactionDao transactionDao;

    @Autowired
    IConvertedFileDao convertedFileDao;

    List<Transaction> transactions = new ArrayList();
    ArrayList<Invoice> invoices;
    Invoice invoice;
    int idTransaction;
    static int totalSpace = 10240;
    double invoicePrice;

    // Dropbox config
    final String APP_KEY = "3xt31on71g5n2d6";
    final String APP_SECRET = "01hgg9uje17vwwv";
    final String URL_SITE = "http://localhost:8080/dashboard/auth";
    DbxWebAuth webAuth;
    DbxClientV2 client;
    DbxRequestConfig config;

    public void setTransactionDao(ITransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    public void setConvertedFileDao(IConvertedFileDao convertedFileDao) {
        this.convertedFileDao = convertedFileDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getInvoices(HttpServletRequest request, Model model) {
        userData = (LoginForm) request.getSession().getAttribute("LOGGEDIN_USER");
        user = (UserAccount) request.getSession().getAttribute("USER_INFORMATIONS");
        DOWNLOAD_PATH = request.getServletContext().getRealPath("/") + "upload/"
                + user.getId() + "/";
        INVOICE_PATH = DOWNLOAD_PATH + "invoices/";
        new File(this.INVOICE_PATH).mkdirs();
        invoices = new ArrayList();
        getHistory(request.getRealPath("/WEB-INF/rabbitConfig.xml"));
        return getDashBoardModelAndView(model);
    }

    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getParameter("fileName");
        if (fileName == null || fileName.equals("")) {
            throw new ServletException("File Name can't be null or empty");
        }
        File file = new File(DOWNLOAD_PATH, fileName);
        if (file.exists()) {
            //throw new ServletException("File doesn't exists on server.");
            System.out.println("File location on server::" + file.getAbsolutePath());
            ServletContext ctx = request.getServletContext();
            InputStream fis = new FileInputStream(file);
            String mimeType = ctx.getMimeType(file.getAbsolutePath());
            response.setContentType(mimeType != null ? mimeType : "application/octet-stream");
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

            ServletOutputStream os = response.getOutputStream();
            byte[] bufferData = new byte[1024];
            int read = 0;
            while ((read = fis.read(bufferData)) != -1) {
                os.write(bufferData, 0, read);
            }
            os.flush();
            os.close();
            fis.close();
            System.out.println("File downloaded at client successfully");
        }
    }

    @RequestMapping(value = "/downloadFileDb", method = RequestMethod.GET)
    protected String uploadInDb(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JsonReader.FileLoadException {
        String accessToken = (String) request.getSession().getAttribute("ACCESS_TOKEN");
        config = new DbxRequestConfig(
                "JavaTutorial/1.0", Locale.getDefault().toString());
        if (accessToken != null && accessToken != "") {
            client = new DbxClientV2(config, accessToken);
            String fileName = request.getParameter("fileName");
            if (fileName == null || fileName.equals("")) {
                throw new ServletException("File Name can't be null or empty");
            }
            File file = new File(DOWNLOAD_PATH, fileName);
            if (file.exists()) {
                //throw new ServletException(\"File doesn't exists on server.");
                System.out.println("File location on server::" + file.getAbsolutePath());

                try {
                    InputStream in = new FileInputStream(file.getAbsolutePath());
                    FileMetadata metadata = client.files().uploadBuilder("/" + fileName).uploadAndFinish(in);
                } catch (DbxException e) {
                    e.printStackTrace();
                }
            }

        } else {
            return "redirect:" + getDropBoxUrl(request);
        }
        return "redirect:/dashboard";
    }

    private void initTransaction(Transaction transaction) {
        invoice = new Invoice();
        invoicePrice = 0.0;
        invoice.setDate(transaction.getDate());
        invoice.setInvoiceId(transaction.getId());
        idTransaction = transaction.getIdTransaction();
    }

    private void getHistory(String path) {
        // Get transactions from bdd
        transactions = transactionDao.getTransByUser(user);

        if (transactions.size() > 0) {
            // Get the first transaction and init parameters
            Transaction transactionTest = transactions.get(0);
            initTransaction(transactionTest);
            for (Transaction aTransaction : transactions) {

                System.out.println("Price :" + invoicePrice + "id " + aTransaction.getIdTransaction());
                // if the transaction is different, create new transaction
                if (aTransaction.getIdTransaction() != idTransaction) {
                    invoice.setPrice(Math.round(invoicePrice * 100.0) / 100.0);
                    invoices.add(invoice);
                    getPdfFromHtml(this.INVOICE_PATH + "facture_" + invoice.getInvoiceId() + ".pdf", generateFacture(invoice));

                    invoice = new Invoice();
                    //jsonTransactions.put(jsonTransaction);
                    initTransaction(aTransaction);
                }
                invoicePrice += aTransaction.getPrix();
                ConvertedFile cVideo = aTransaction.getConvertedFiles();
                if (cVideo != null) {
                    String newName = cVideo.getOriginalName().replace(cVideo.getOldType(), cVideo.getNewType());
                    cVideo.setOriginalName(newName);
                    invoice.addConvertedFile(cVideo);
                    // If video is not converted yet start conversion
                    if (aTransaction.getPayed() && (cVideo.getConverted() == null || !cVideo.getConverted()) && (cVideo.getInConvertion() == null || !cVideo.getInConvertion())) {
                        FileIsUploaded videoToConvert = new FileIsUploaded(cVideo.getId(), cVideo.getFilePath(), cVideo.getNewType(), cVideo.getNewEncoding());
                        RabbitInit init = new RabbitInit(path);
                        init.getFileUploadedSender().send(videoToConvert);
                        init.closeAll();
                        cVideo.setConverted(false);
                        cVideo.setInConvertion(true);
                        convertedFileDao.updateFile(cVideo);
                        System.out.println("Conversion du fichier : " + cVideo.getOriginalName());
                    }
                }

            }
            invoice.setPrice(Math.round(invoicePrice * 100.0) / 100.0);
            invoices.add(invoice);
            getPdfFromHtml(this.INVOICE_PATH + "facture_" + invoice.getInvoiceId() + ".pdf", generateFacture(invoice));
        }

    }

    // DropBox auth
    private String getDropBoxUrl(HttpServletRequest request) throws JsonReader.FileLoadException {
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
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
            response.sendError(400);
        } catch (DbxWebAuth.BadStateException ex) {
            // Send them back to the start of the auth flow.
            response.sendRedirect("/dashboard");
        } catch (DbxWebAuth.CsrfException ex) {
        } catch (DbxWebAuth.NotApprovedException ex) {
            // When Dropbox asked "Do you want to allow this app to access your
            // Dropbox account?", the user clicked "No".
        } catch (DbxWebAuth.ProviderException ex) {
            response.sendError(503, "Error communicating with Dropbox.");
        } catch (DbxException ex) {
            response.sendError(503, "Error communicating with Dropbox.");
        }

        String accessToken = authFinish.getAccessToken();
        request.getSession().setAttribute("ACCESS_TOKEN", accessToken);
        client = new DbxClientV2(config, accessToken);
        return "redirect:/dashboard";
    }

    @RequestMapping(value = "/deleteFile", method = RequestMethod.GET)
    public String deleteFile(HttpServletRequest request, HttpServletResponse resp, Model model) {
        if (request.getParameter("fileName") != null && !request.getParameter("fileName").isEmpty()) {
            File file = new File(DOWNLOAD_PATH + request.getParameter("fileName"));
            File thumb = new File(DOWNLOAD_PATH + "thumb_" + request.getParameter("fileName"));
            int invoiceNumber = Integer.parseInt(request.getParameter("invoiceNumber"));
            String filePath = DOWNLOAD_PATH + request.getParameter("fileName");
            java.nio.file.Path p = Paths.get(filePath);
            String fileName = p.getFileName().toString();
            Invoice invoiceToDelete = invoices.get(invoiceNumber);
            int index = 0;
            for (ConvertedFile video :
                    invoiceToDelete.getConvertedFiles()) {
                if (video.getOriginalName().equals(fileName)) {
                    // delete file from invoices
                    // Delete path to the file
                    invoiceToDelete.getConvertedFiles().get(index).setFilePath("");
                    // Delete his size for the space user size
                    invoiceToDelete.getConvertedFiles().get(index).setSize(0.0);
                    // Update file in bdd
                    convertedFileDao.updateFile(invoiceToDelete.getConvertedFiles().get(index));

                    // Delete file from SAN
                    if (file.exists()) {
                        file.delete();
                    }
                    if (thumb.exists()) {
                        thumb.delete();
                    }
                    resp.setStatus(200);
                }
                index++;
            }
        }
        return "redirect:/dashboard";
    }

    public ModelAndView getDashBoardModelAndView(Model model) {
        double userSize = user.getTotalSize();
        System.out.println("Taile totale :" + user.getTotalSize() + " &" + userSize);
        double spaceLeft = (userSize / totalSpace) * 100;
        model.addAttribute("invoices", invoices);
        model.addAttribute("spacePercent", spaceLeft);
        model.addAttribute("userSize", userSize);
        if (invoices.size() > 0 && invoices.get(0).getConvertedFiles().size() > 0) {
            model.addAttribute("videoStream", invoices.get(0).getConvertedFiles().get(0).getFilePath());
        }
        model.addAttribute("userAccount", new UserAccount());
        return new ModelAndView("dashboard", "model", model);
    }

    @RequestMapping(value = "/getInvoice", method = RequestMethod.GET)
    public void getPdfInvoice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String invoiceId = (String) request.getParameter("invoiceId");
        String fileName = "facture_" + invoiceId + ".pdf";
        File file = new File(INVOICE_PATH, fileName);
        if (file.exists()) {
            //throw new ServletException("File doesn't exists on server.");
            System.out.println("File location on server::" + file.getAbsolutePath());
            ServletContext ctx = request.getServletContext();
            InputStream fis = new FileInputStream(file);
            String mimeType = ctx.getMimeType(file.getAbsolutePath());
            response.setContentType(mimeType != null ? mimeType : "application/octet-stream");
            response.setContentLength((int) file.length());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");

            ServletOutputStream os = response.getOutputStream();
            byte[] bufferData = new byte[1024];
            int read = 0;
            while ((read = fis.read(bufferData)) != -1) {
                os.write(bufferData, 0, read);
            }
            os.flush();
            os.close();
            fis.close();
        }

    }

    private String generateFacture(Invoice invoice) {
        String str = "<html><head><title></title></head>"
                + "<body>"
                + "<h1>Facture numero " + invoice.getInvoiceId() + "</h1>"
                + "<br/><p>Dead pineapple</p>"
                + "<p>25 rue du sapin</p>"
                + "<p>75016 Paris</p><br/><br/><br/>"
                + "<p></p><p>"
                + user.getLastName() + " " + "" + user.getFirstName() + "</p>"
                + "<p>" + (user.getAdresse() == null ? "" : user.getAdresse()) + "</p>"
                + "<p>" +( user.getCodePostal() == 0 ? "" : user.getCodePostal())
                + "<br/><p><strong>Méthode de payment:</strong> Paypal</p><br/><br/></p>"
                + "<table border=\"1\" style=\"border-color:#DDD;margin-top:30px;\" >";
        str += "<tr><th>Name</th><th>Convertion</th><th>Encoding</th><th>Prix</th></tr>";
        for (ConvertedFile file : invoice.getConvertedFiles()) {
            str += "<tr>";
            str += "<td>" + file.getOriginalName() + "</td>";
            str += "<td>" + file.getOldType() + " -> " + file.getNewType() + "</td>";
            str += "<td>" + (file.getNewEncoding()==null?"":file.getNewEncoding()) + "</td>";
            str += "<td>" + "</td>";
            str += "</tr>";
        }
        str += "<tr><td></td><td></td><td><strong>Total :</strong></td><td>" + invoice.getPrice() + "€</td></tr>";
        str += "</table></body>";
        return str;
    }

    private void getPdfFromHtml(String filename, String html) {
        try {
            if (!new File(filename).exists()) {
                OutputStream file = new FileOutputStream(new File(filename));
                Document document = new Document();
                PdfWriter.getInstance(document, file);
                document.open();
                HTMLWorker htmlWorker = new HTMLWorker(document);
                htmlWorker.parse(new StringReader(html));
                document.close();
                file.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
