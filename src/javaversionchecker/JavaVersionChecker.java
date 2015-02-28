/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaversionchecker;

import java.io.File;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author John
 */
public class JavaVersionChecker {
    private static final Double MINIMUM_JAVA_VERSION = 1.7;
    private static final String PROGRAM_NAME = "Res-Ident";
    private static final String JAR_PROGRAM_PATH = "java -jar ." + File.separator + "dist" + File.separator + "Resident_Identifier.jar";
    private static final String WINDOWS_x86_UPDATE_PATH = "." + File.separator + "updates" + File.separator + "run-x86.bat";
    private static final String WINDOWS_x64_UPDATE_PATH = "." + File.separator + "updates" + File.separator + "run-x64.bat";
    private static final String MAC_UPDATE_PATH = "." + File.separator + "updates" + File.separator + "jre-8u31-macosx-x64.dmg";
    private static final String UPDATE_MESSAGE_PROMPT = 
            "Your java version is out of date and must be updated before running " + PROGRAM_NAME + "."
            + "\nWould you like to update? "
            + "\n(" + PROGRAM_NAME + " will launch as soon as Java is updated)";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Double version = Double.parseDouble(System.getProperty("java.specification.version"));
        if(version < MINIMUM_JAVA_VERSION && 
                popup(UPDATE_MESSAGE_PROMPT, true))
        {
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            
            if ((OS.contains("mac")) || (OS.contains("darwin"))) {//Yay! Apple install!
                updateMac();
            } else if (OS.contains("win")) {//Whoo hoo! Windows install!
                updateWindows();
            } else {//god help you. You've choosen this path. Sort it yourself.
                updateOther();
            }
            
            version = Double.parseDouble(System.getProperty("java.specification.version"));
        }
        
        if(version >= MINIMUM_JAVA_VERSION)
            System.out.println("Launch program returned: " + launchProgram(JAR_PROGRAM_PATH, false));
        else
            popup("Please update your java before running this program.", false);
        System.exit(0);        
    }
    
    private static void updateWindows()
    {
        String arch = System.getProperty("os.arch");
        if(arch.contains("64")){
            System.out.println("Update Windows x64 program returned: " + launchProgram(WINDOWS_x64_UPDATE_PATH, true));
        } else if(arch.contains("86")) {
            System.out.println("Update Windows x86 returned: " + launchProgram(WINDOWS_x86_UPDATE_PATH, true));
        } else {
            popup("I don't recognize this Windows architecture, Please update java manually. (www.java.com)", false);
            System.exit(0);
        }
    }
    
    private static void updateMac()
    {
       System.out.println("Update Mac program returned: " + launchProgram(new String[] {"/usr/bin/open", MAC_UPDATE_PATH}, true));
    }
    
    private static void updateOther()
    {
        popup("Sorry, you need to update java manually before launching " + PROGRAM_NAME + ". (www.java.com)", false);
        System.exit(0);
    }
    
    private static boolean launchProgram(final String PROG_PATH, boolean block)
    {
        try {
            Process proc = Runtime.getRuntime().exec(PROG_PATH);
            if(block)
                proc.waitFor();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(JavaVersionChecker.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    private static boolean launchProgram(final String[] PROG_ARGS, boolean block)
    {
        try {
            Process proc = Runtime.getRuntime().exec(PROG_ARGS);
            if(block)
                proc.waitFor();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(JavaVersionChecker.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    /**
     * Returns true if user clicks yes. 
     * Note: blocking method (will not return until user acts upon options)
     * 
     * @param text text to display yes or no question to user (update or not)
     * @param yesNoQuestion if true, options will be 'yes' and 'no', else it will be 'ok' and return true
     * @return true if user selects yes, false if other wise (no, exits window, etc). Returns true upon user action if yesNoQuestion is false.
     */
    private static boolean popup(final String text, boolean yesNoQuestion)
    {
        JOptionPane pane = new JOptionPane(text, JOptionPane.QUESTION_MESSAGE, ((yesNoQuestion)? JOptionPane.OK_CANCEL_OPTION : JOptionPane.DEFAULT_OPTION));
        final JDialog diag = pane.createDialog("Message");
        diag.setVisible(true);
        
        return pane.getValue() != null && (int)pane.getValue() == JOptionPane.OK_OPTION;
    }
}
