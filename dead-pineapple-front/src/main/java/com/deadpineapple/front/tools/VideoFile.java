package com.deadpineapple.front.tools;

import com.deadpineapple.dal.entity.ConvertedFile;
import com.deadpineapple.videoHelper.information.VideoInformation;

import javax.persistence.Transient;
import java.util.ArrayList;

/**
 * Created by saziri on 19/04/2016.
 */
public class VideoFile{

    public VideoInformation getVideoInformation() {
        return videoInformation;
    }

    public void setVideoInformation(VideoInformation videoInformation) {
        this.videoInformation = videoInformation;
    }

    public ConvertedFile getConvertedFile() {
        return convertedFile;
    }

    public void setConvertedFile(ConvertedFile convertedFile) {
        this.convertedFile = convertedFile;
    }

    VideoInformation videoInformation;
    ConvertedFile convertedFile;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    Double price;
    public static ArrayList<VideoFile> deleteVideoInformation(ArrayList<VideoFile> vfs, ConvertedFile cf){
         for(int i = 0; i < vfs.size(); i++){
             VideoFile vf = vfs.get(i);
             if(vf.getConvertedFile().getOriginalName().equals(cf.getOriginalName())){
                 vfs.remove(i);
             }
         }
        return vfs;
    }
}
