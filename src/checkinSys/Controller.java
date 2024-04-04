package checkInSimulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JSlider;

import GUI.AirportGUI;

public class Controller {
	public Controller(AirportGUI airport, SharedObject so) {
		airport.setSharedObject(so);
		startSpeedSlider(airport);
		new Thread(airport).start();
		for(CheckInDesk desk: airport.getDesk())
			startCheckInDeskBox(desk);
	}

	public void startCheckInDeskBox(CheckInDesk desk) {
			desk.getButton().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JCheckBox jcb = (JCheckBox) e.getSource(); // �õ��������¼�
					if (jcb.isSelected()) {
						desk.getButtonPanel().setBorder(BorderFactory.createLineBorder(Color.RED)); 
						jcb.setText(desk.getDeskType()+ ' ' + desk.getDeskName() + " Close"); 
						jcb.setForeground(Color.red);
						desk.getText().setText("");
						Logger.log(Logger.LogLevel.INFO, desk.getDeskType()+ ' ' + desk.getDeskName() + " Close"); 
						desk.closeDesk();
					}
					else {
						desk.getButtonPanel().setBorder(BorderFactory.createLineBorder(Color.GREEN)); 
						jcb.setText(desk.getDeskType()+ ' ' + desk.getDeskName() + " Open");
						jcb.setForeground(Color.black);
						Logger.log(Logger.LogLevel.INFO, desk.getDeskType()+ ' ' + desk.getDeskName() + " Open"); 
						desk.restartDesk();
					}
				}
			});
		}

	public void startSpeedSlider(AirportGUI airport) {
		airport.getSpeedSlider().addChangeListener(e -> {
			JSlider source = (JSlider) e.getSource();
			if (!source.getValueIsAdjusting()) {
				airport.setTimerSpeed(1000000 / source.getValue());

				// Adjust all timers according to the new speed
				airport.adjustTimerSpeeds();
			}
		});
	}

	public static void main(String[] args) {
		Logger logger = Logger.getInstance();

		SharedObject so = new SharedObject();
		AirportGUI airport = new AirportGUI(so);
		Controller c = new Controller(airport, so);
	}

}
