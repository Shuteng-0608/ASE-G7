package GUI;

import javax.swing.*;

import checkInSimulation.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

public class AirportGUI extends JFrame implements Runnable{
    private JPanel queuePanel;
    private JPanel deskPanel;
    private JPanel flightPanel;
    private JSlider speedSlider;
    JTextArea queue1Text;
    JTextArea queue2Text;
    private Random timeSetter = new Random();
    private final int deskWidth = 150; // ���ù�̨���ڹ̶����
    private final int deskHeight = 150; // ���ù�̨���ڹ̶��߶�
    private int queueCount1 = 0;  
    private int queueCount2 = 0;  
    private int queueNum = 2;
    private int deskNum = 5;
    private SharedObject so;
    private List<Thread> checkInThreads = new ArrayList<>();
    private List<Thread> queueThreads = new ArrayList<>();
    private final int queuePanelHeight = 200; // ���ö������̶��߶�

    private int timerSpeed = 1000; // ��ʼ�ٶ�����Ϊ1000���룬��1��

    // �洢ÿ�������ʱ�������ã��Ա���Ը��ݻ������ı仯������ʱ���ٶ�
    private Map<Integer, Timer> flightTimers = new HashMap<>();
    public AirportGUI() {
    	so = new SharedObject();
        setTitle("Airport Check-in System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // ���ò��ֹ��������
        
        JPanel queuePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        queuePanel.setPreferredSize(new Dimension(getWidth(), queuePanelHeight)); // ���ù̶��߶�
        queuePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        queue1Text = new JTextArea("There are currently "+ queueCount1 +" people in queue1");
        
        queue1Text.setEditable(false);
        JScrollPane queue1Scroll = new JScrollPane(queue1Text);
        queuePanel.add(queue1Scroll);
        
        queue2Text = new JTextArea("There are currently "+ queueCount2 +" people in queue2");
        queue2Text.setEditable(false);
        JScrollPane queue2Scroll = new JScrollPane(queue2Text);
        queuePanel.add(queue2Scroll);

        createQueuePanel(); // ����������ʾ��
        deskPanel = createDeskPanel();   // ������̨������
        flightPanel = createFlightPanel(); // ����������Ϣ��
        speedSlider = createSpeedSlider();

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(flightPanel, BorderLayout.CENTER);
        bottomPanel.add(speedSlider, BorderLayout.SOUTH);

        add(queuePanel, BorderLayout.NORTH);
        add(deskPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        pack(); // ������������Ӧ�����С
        setVisible(true);
//        for (Thread thread : queueThreads) {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }
                
    private void createQueuePanel() {
    	for (int i = 1; i <= queueNum; i++) {
			PassengerQueue queue = new PassengerQueue("economy " + i, so);
			Thread thread = new Thread(queue);
			queueThreads.add(thread);
	        thread.start();
		}
    }

    private JPanel createDeskPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 5, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // ��ӱ߾�
//        for (int i = 1; i <= deskNum; i++) {
//			CheckInDesk queue = new CheckInDesk("Desk " + i, so);
//			Thread thread = new Thread(queue);
//			checkInThreads.add(thread);
//	        thread.start();
//		}
//        
//        for (Thread thread : checkInThreads) {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        for (int i = 1; i <= 5; i++) {
            final int deskNumber = i; // ʹ��final�����Ա�����������ʹ��
            JPanel desk = new JPanel();
            desk.setPreferredSize(new Dimension(deskWidth, deskHeight)); // ���ù�̨�̶���С
            desk.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // ��ʼ�߿�����Ϊ��ɫ
            JCheckBox checkBox = new JCheckBox("Desk " + deskNumber + " Close");
            checkBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JCheckBox cb = (JCheckBox) e.getSource();
                    if (cb.isSelected()) {
                        desk.setBorder(BorderFactory.createLineBorder(Color.GREEN)); // ��ѡʱ�߿����
                        cb.setText("Desk " + deskNumber + " Open"); // ���¸�ѡ���ı�ΪOpen
                    } else {
                        desk.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // δ��ѡʱ�߿��غ�ɫ
                        cb.setText("Desk " + deskNumber + " Close"); // ���¸�ѡ���ı�Ϊ��ʼ״̬
                    }
                }
            });

            desk.add(checkBox);
            panel.add(desk);
        }

        return panel;
    }


    private JPanel createFlightPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(getWidth(), queuePanelHeight)); // �����Ѿ�������queuePanelHeight

        for (int i = 1; i <= 6; i++) {
            final int flightNumber = i;
            JPanel flightPanel = new JPanel(new BorderLayout());
            flightPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel countdownLabel = new JLabel("", SwingConstants.CENTER);
            flightPanel.add(countdownLabel, BorderLayout.CENTER);

            // ���õ���ʱʱ��Ϊ1����
            final int[] timeLeft = {60 + timeSetter.nextInt(200)}; // ����Ϊ��λ

            Timer timer = new Timer(1000, new ActionListener() { // ��ʱ��ÿ�봥��һ��
                public void actionPerformed(ActionEvent e) {
                    timeLeft[0]--;
                    countdownLabel.setText("Flight " + flightNumber + " - " + timeLeft[0]/60 + "M" + timeLeft[0]%60 + "S");

                    if (timeLeft[0] <= 0) {
                        flightPanel.setBorder(BorderFactory.createLineBorder(Color.RED)); // �߿���
                        countdownLabel.setText("Flight " + flightNumber + " Closed"); // �ı�����ΪClosed
                        ((Timer)e.getSource()).stop(); // ֹͣ��ʱ��
                    }
                }
            });
            timer.start(); // ������ʱ��
            flightTimers.put(flightNumber, timer); // Store the timer reference

            panel.add(flightPanel);
        }

        return panel;
    }

    private JSlider createSpeedSlider() {
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, 250, 4000, timerSpeed);
        speedSlider.setMajorTickSpacing(500);
        speedSlider.setMinorTickSpacing(250);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(250, new JLabel("X0.25"));
        labelTable.put(500, new JLabel("X0.5"));
        labelTable.put(1000, new JLabel("X1"));
        labelTable.put(2000, new JLabel("X2"));
        labelTable.put(3000, new JLabel("X3"));
        labelTable.put(4000, new JLabel("X4"));
        speedSlider.setLabelTable(labelTable); // ���û������ı�ǩ
        speedSlider.addChangeListener(e -> {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                timerSpeed = 1000000/source.getValue() ;
                // Adjust all timers according to the new speed
                adjustTimerSpeeds();
            }
        });

        return speedSlider;
    }

    private void adjustTimerSpeeds() {
        for (Map.Entry<Integer, Timer> entry : flightTimers.entrySet()) {
            Timer timer = entry.getValue();
            if (timer != null) {
                timer.setDelay(timerSpeed);
                // Note: This does not restart the timer; it only adjusts the delay for future ticks.
            }
        }
    }
    
    @Override
	public void run() {
		while (true) {
			try {
			Thread.sleep(2000);//Thread.Sleep()�������ڽ���ǰ�߳�����һ��ʱ�䣬��λΪ���롣����Ϊÿ1000��������һ���̡߳�
			queueCount1 = so.getQueue1().size();
            Queue<Passenger> q1 = so.getQueue1();
            queue1Text.setText("There are currently "+ queueCount1 +" people in queue1");
            for (Passenger p : q1) {
            	queue1Text.append("\n" + p.getFlight() + "\t" +  p.getName()+ "         " + "\t" + p.getWeight() + "\t" + p.getVolume() );
            	queue1Text.paintImmediately(queue1Text.getBounds());
            }
            
            queueCount2 = so.getQueue2().size();
            queue2Text.setText("There are currently "+ queueCount2 +" people in queue2");
            Queue<Passenger> q2 = so.getQueue2();
            for (Passenger p : q2) {
            	queue2Text.append("\n" + p.getFlight() + "\t" +  p.getName() + "         " + "\t" + p.getWeight() + "\t" + p.getVolume() );
            	queue2Text.paintImmediately(queue2Text.getBounds());
            }
			} catch (InterruptedException e) {
				e.printStackTrace();//�׳��쳣
			}
		}
	}

    public static void main(String[] args) {
    	AirportGUI a = new AirportGUI();
    	new Thread(a).start();//�����߳�
    }
}