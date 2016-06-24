package com.deadpineapple.videoHelper;

import com.deadpineapple.videoHelper.fileEdit.FileJoiner;
import com.deadpineapple.videoHelper.fileEdit.FileSplitter;
import com.deadpineapple.videoHelper.information.VideoInformation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 15256 on 31/03/2016.
 */
public class TestLauncher {
    public static void main(String[] args) {

        FileSplitter vid = new FileSplitter("D:\\serie\\into badlands\\s1\\Into.The.Badlands.S01E01.FASTSUB.VOSTFR.1080p.WEB-DL.HEVC.H265-Yn1D.mkv");
        try {
           List<File> fls = vid.splitFile();
      //    List<File> fls = new ArrayList<File>();
      //      for (int i = 0; i < 16; i++) {
      //          fls.add(new File("D:\\serie\\into badlands\\s1\\"+i+"_Into.The.Badlands.S01E01.FASTSUB.VOSTFR.1080p.WEB-DL.HEVC.H265-Yn1D.mkv"));
      //      }

            FileJoiner joiner = new FileJoiner(fls, "D:\\serie\\into badlands\\s1\\output.mkv");
            joiner.joinFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
