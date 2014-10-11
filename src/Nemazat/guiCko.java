package Nemazat;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ViaHeader;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JScrollPane;

public class guiCko extends JFrame {

	private JPanel contentPane;
	public static JList L_tranzakcie,L_dialogy,L_metody;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private int index=0;
	public String metoda;
	/**
	 * Launch the application.
	 */
	public static void nakresli() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					guiCko frame = new guiCko();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public guiCko() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 624, 590);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblDialog = new JLabel("Dialog");
		lblDialog.setBounds(245, 34, 46, 14);
		contentPane.add(lblDialog);
		
		JLabel lblTranzakcia = new JLabel("Tranzakcia");
		lblTranzakcia.setBounds(255, 382, 70, 14);
		contentPane.add(lblTranzakcia);
		
		JLabel lblMetody = new JLabel("Metody");
		lblMetody.setBounds(255, 214, 46, 14);
		contentPane.add(lblMetody);
		
		L_metody = new JList();
		L_metody.setVisibleRowCount(400);
		L_metody.setBounds(46, 407, 506, 114);
		contentPane.add(L_metody);
		
		L_tranzakcie = new JList();
		L_tranzakcie.setVisibleRowCount(400);
		L_tranzakcie.setBounds(46, 239, 506, 114);
		contentPane.add(L_tranzakcie);
		
		
		L_dialogy = new JList();
		L_dialogy.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				SipLayer.listTranzakcii.removeAllElements();
				String cisloCall = L_dialogy.getSelectedValue().toString();
				cisloCall = cisloCall.substring(cisloCall.indexOf("ID:")+4,cisloCall.length());
				//zistenie CallId pre rozpoznanie novych tranzakcii
				index = 0;
				for(int i = 0;i<SipLayer.list_dialog.size();i++)
				{
				
				CallIdHeader call = (CallIdHeader)SipLayer.list_dialog.get(i).getHeader("Call-ID");
				if(call.getCallId().equals(cisloCall))
				{
				index++;
				metoda = SipLayer.list_dialog.get(i).getMethod();
				ViaHeader braanch = (ViaHeader)SipLayer.list_dialog.get(i).getHeader("Via");
				CSeqHeader cseq = (CSeqHeader)SipLayer.list_dialog.get(i).getHeader("CSeq");
				String vypis = index + ": Metoda: " +metoda+" Branch: "+braanch.getBranch()+" CSeq: "+cseq.getSeqNumber();
				SipLayer.listTranzakcii.addElement(vypis);
				}
				}
			}
		});
		
		L_dialogy.setVisibleRowCount(400);
		L_dialogy.setValueIsAdjusting(true);
		L_dialogy.setBounds(47, 55, 505, 139);
		contentPane.add(L_dialogy);
		
		scrollPane = new JScrollPane(L_dialogy);
		scrollPane.setBounds(46, 55, 506, 139);
		contentPane.add(scrollPane);
		
		scrollPane_1 = new JScrollPane(L_tranzakcie);
		scrollPane_1.setBounds(46, 239, 506, 114);
		contentPane.add(scrollPane_1);
		
		scrollPane_2 = new JScrollPane(L_metody);
		scrollPane_2.setBounds(46, 407, 506, 114);
		contentPane.add(scrollPane_2);
	}
}
