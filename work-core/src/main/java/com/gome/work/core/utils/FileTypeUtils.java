package com.gome.work.core.utils;

/**
 * Created by songzhiyang on 2017/2/25.
 */

public class FileTypeUtils {
    public static boolean isDoc(String type) {
        type = type.toLowerCase();
        switch (type) {
            case "doc":
            case "docx":
            case "ppt":
            case "pptx":
            case "xls":
            case "xlsx":
            case "vsd":
            case "pot":
            case "pps":
            case "rtf":
            case "wps":
            case "et":
            case "dps":
            case "pdf":
            case "txt":
            case "epub":
            case "xlt":
            case "xltx":
            case "potx":
            case "dot":
            case "dotx":
            case "ppsx":
                return true;
            default:
                return false;
        }
    }

    public static boolean isVideo(String type) {
        type = type.toLowerCase();
        switch (type) {
            case "mp4":
            case "avi":
            case "rm":
            case "asf":
            case "wmv":
            case "mov":
            case "3gp":
            case "rmvb":
            case "avs":
            case "flv":
            case "mkv":
            case "mpg":
            case "dat":
            case "ogm":
            case "vob":
            case "ts":
            case "tp":
            case "ifo":
            case "nsv":
            case "m2ts":
            case "swf":
                return true;
            default:
                return false;
        }
    }

    public static boolean isVoice(String type) {
        type = type.toLowerCase();
        switch (type) {
            case "wav":
            case "mp3":
            case "ra":
            case "rma":
            case "wma":
            case "ogg":
            case "ape":
            case "flac":
            case "acc":
            case "mpc":
            case "aac":
            case "au":
            case "aiff":
            case "mod":
            case "asf":
            case "cda":
            case "mid":
            case "mka":
            case "mpa":
            case "ofr":
            case "wv":
            case "tta":
            case "ac3":
            case "dts":
                return true;
            default:
                return false;
        }
    }

    public static boolean isImage(String type) {
        type = type.toLowerCase();
        switch (type) {
            case "bmp":
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
                return true;
            default:
                return false;
        }
    }

    public static boolean canRead(String type) {
        type = type.toLowerCase();
        switch (type) {
            case "doc":
            case "docx":
            case "xls":
            case "xlsx":
            case "pdf":
            case "txt":
            case "ppt":
            case "pptx":
                return true;
            default:
                return false;
        }
    }

    public static boolean canSend(String type) {
        type = type.toLowerCase();
        switch (type) {
            case "com":
            case "bat":
            case "chm":
            case "hlp":
            case "htm":
            case "html":
            case "js":
            case "msi":
            case "scr":
            case "vbs":
            case "reg":
                return false;
            default:
                return true;
        }
    }
}
