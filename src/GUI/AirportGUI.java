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
    private JPanel deskControlPanel;
    private JSlider speedSlider;
    JTextArea queue1Text;
    JTextArea queue2Text;
    JTextArea desk1Text;
    JTextArea desk2Text;
    JTextArea desk3Text;
    JTextArea desk4Text;
    JTextArea desk5Text;
    private Random timeSetter = new Random();
    private final int deskWidth = 150; // ���ù�̨���ڹ̶����
    private final int deskHeight = 50; // ���ù�̨���ڹ̶��߶�
    private int queueCount1 = 0;  
    private int queueCount2 = 0;  
    private int queueNum = 2;
    private int deskNum = 5;
    private SharedObject so;
    private List<Thread> checkInThreads = new ArrayList<>();
    private List<CheckInDesk> Desk = new ArrayList<>();
    private List<Thread> queueThreads = new ArrayList<>();
    private final int queuePanelHeight = 200; // ���ö������̶��߶�
    private List<Passenger> curPassengerList = new ArrayList<>();

    private int timerSpeed = 1000; // ��ʼ�ٶ�����Ϊ1000���룬��1��

    // �洢ÿ�������ʱ�������ã��Ա���Ը��ݻ������ı仯������ʱ���ٶ�
    private Map<Integer, Timer> flightTimers = new HashMap<>();
    public AirportGUI() {
    	so = new SharedObject();
        setTitle("Airport Check-in System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // ���ò��ֹ��������
        
        // For passenger queue
        queuePanel = new JPanel(new GridLayout(1, 2, 10, 10));
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
        
        
        // For check-in desk
        deskControlPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        deskControlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // ��ӱ߾�
        
        deskPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        deskPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // ��ӱ߾�
        
        desk1Text = new JTextArea("desk1");
        desk1Text.setEditable(false);
        desk1Text.setPreferredSize(new Dimension(150, 100));
        desk1Text.setLineWrap(true);
        desk1Text.setWrapStyleWord(true);
        deskPanel.add(desk1Text);
        
        desk2Text = new JTextArea("desk2");
        desk2Text.setEditable(false);
        desk2Text.setPreferredSize(new Dimension(150, 100));
        desk2Text.setLineWrap(true);
        desk2Text.setWrapStyleWord(true);
        deskPanel.add(desk2Text);
        
        desk3Text = new JTextArea("desk3");
        desk3Text.setEditable(false);
        desk3Text.setPreferredSize(new Dimension(150, 100));
        desk3Text.setLineWrap(true);
        desk3Text.setWrapStyleWord(true);
        deskPanel.add(desk3Text);
        
        desk4Text = new JTextArea("desk4");
        desk4Text.setEditable(false);
        desk4Text.setPreferredSize(new Dimension(150, 100));
        desk4Text.setLineWrap(true);
        desk4Text.setWrapStyleWord(true);
        deskPanel.add(desk4Text);
        
        desk5Text = new JTextArea("desk5");
        desk5Text.setEditable(false);
        desk5Text.setPreferredSize(new Dimension(150, 100));
        desk5Text.setLineWrap(true);
        desk5Text.setWrapStyleWord(true);
        deskPanel.add(desk5Text);

        
        // Open queue and desk threads
        createQueue(); 
        createDesk();
        
        deskControlPanel = createControlDeskPanel();   // ������̨������
        flightPanel = createFlightPanel(); // ����������Ϣ��
        speedSlider = createSpeedSlider();
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(deskControlPanel, BorderLayout.CENTER);
        centerPanel.add(deskPanel, BorderLayout.SOUTH);
        

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(flightPanel, BorderLayout.CENTER);
        bottomPanel.add(speedSlider, BorderLayout.SOUTH);

        add(queuePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        pack(); 
        setVisible(true);

    }
                
    private void createQueue() {
    	for (int i = 1; i <= queueNum; i++) {
			PassengerQueue queue = new PassengerQueue("economy " + i, so);
			Thread thread = new Thread(queue);
			queueThreads.add(thread);
	        thread.start();
		}
    }
    
    private void createDesk() {
    	for (int i = 1; i <= deskNum; i++) {
			CheckInDesk queue = new CheckInDesk("Desk " + i, so);
			Thread thread = new Thread(queue);
			checkInThreads.add(thread);
			Desk.add(queue);
	        thread.start();
		}
    }

    private JPanel createControlDeskPanel() {
        
        for (int i = 1; i <= 5; i++) {
            final int deskNumber = i; // ʹ��final�����Ա�����������ʹ��
            JPanel desk = new JPanel();
            desk.setPreferredSize(new Dimension(deskWidth, deskHeight)); // ���ù�̨�̶���С
            desk.setBorder(BorderFactory.createLineBorder(Color.GREEN)); // ��ʼ�߿�����Ϊ��ɫ
            JCheckBox checkBox = new JCheckBox("Desk " + deskNumber + " Open");
            checkBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JCheckBox cb = (JCheckBox) e.getSource();
                    if (cb.isSelected()) {
                    	desk.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // δ��ѡʱ�߿��غ�ɫ
                        cb.setText("Desk " + deskNumber + " Close"); // ���¸�ѡ���ı�Ϊ��ʼ״̬
                        for (CheckInDesk desk : Desk) {
                        	if (desk.getDeskName().equals("Desk " + deskNumber)) {
                        		System.out.println("Desk " + deskNumber + " Close");
                        		desk.closeDesk();
                        	}
                        }
                    } else {
                        
                        desk.setBorder(BorderFactory.createLineBorder(Color.GREEN)); // ��ѡʱ�߿����
                        cb.setText("Desk " + deskNumber + " Open"); // ���¸�ѡ���ı�ΪOpen
                    }
                }
            });
            desk.add(checkBox);
            
            deskControlPanel.add(desk);
        }
        return deskControlPanel;
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
				Thread.sleep(1000);//Thread.Sleep()�������ڽ���ǰ�߳�����һ��ʱ�䣬��λΪ���롣����Ϊÿ1000��������һ���̡߳�
				
				// for passenger queue
				queueCount1 = so.getQueue1().size();
	            Queue<Passenger> q1 = so.getQueue1();
	            queue1Text.setText("There are currently "+ queueCount1 +" people in queue1");
	            for (Passenger p : q1) {
	            	queue1Text.append("\n" + p.getFlight() + "\t" +  p.getName()+ "          " + "\t" + p.getWeight() + "\t" + p.getVolume() );
	            	queue1Text.paintImmediately(queue1Text.getBounds());
	            }
	            
	            queueCount2 = so.getQueue2().size();
	            queue2Text.setText("There are currently "+ queueCount2 +" people in queue2");
	            Queue<Passenger> q2 = so.getQueue2();
	            for (Passenger p : q2) {
	            	
	            	queue2Text.append("\n" + p.getFlight() + "\t" +  p.getName() + "          " + "\t" + p.getWeight() + "\t" + p.getVolume() );
	            	queue2Text.paintImmediately(queue2Text.getBounds());
	            }
	            
	            
	            // for check-in desk
	            for (CheckInDesk desk : Desk) {
	            	Passenger p = desk.getClient();
	            	if (p == null) continue;
	            	if (desk.getDeskName().equals("Desk 1")) {
	            		desk1Text.setText(p.getName() + " is dropping off 1 bag of " + p.getWeight() + "kg");
	            		if (p.excess_fee() != 0) {
	            			desk1Text.append("A buggage fee of " + p.excess_fee() + "$ is due");
	            			desk1Text.paintImmediately(desk1Text.getBounds());
	            		} else {
	            			desk1Text.append("\n" + "No baggage fee is due.");
	            			desk1Text.paintImmediately(desk1Text.getBounds());
	            		}
	            	}
					if (desk.getDeskName().equals("Desk 2")) {
						desk2Text.setText(p.getName() + " is dropping off 1 bag of " + p.getWeight() + "kg");
	            		if (p.excess_fee() != 0) {
	            			desk2Text.append("A buggage fee of " + p.excess_fee() + "$ is due");
	            			desk2Text.paintImmediately(desk2Text.getBounds());
	            		} else {
	            			desk2Text.append("\n" + "No baggage fee is due.");
	            			desk2Text.paintImmediately(desk2Text.getBounds());
	            		}
	            	}
					if (desk.getDeskName().equals("Desk 3")) {
						desk3Text.setText(p.getName() + " is dropping off 1 bag of " + p.getWeight() + "kg");
	            		if (p.excess_fee() != 0) {
	            			desk3Text.append("A buggage fee of " + p.excess_fee() + "$ is due");
	            			desk3Text.paintImmediately(desk3Text.getBounds());
	            		} else {
	            			desk3Text.append("\n" + "No baggage fee is due.");
	            			desk3Text.paintImmediately(desk3Text.getBounds());
	            		}
					}
					if (desk.getDeskName().equals("Desk 4")) {
						desk4Text.setText(p.getName() + " is dropping off 1 bag of " + p.getWeight() + "kg");
	            		if (p.excess_fee() != 0) {
	            			desk4Text.append("A buggage fee of " + p.excess_fee() + "$ is due");
	            			desk4Text.paintImmediately(desk4Text.getBounds());
	            		} else {
	            			desk4Text.append("\n" + "No baggage fee is due.");
	            			desk4Text.paintImmediately(desk4Text.getBounds());
	            		}
					}
					if (desk.getDeskName().equals("Desk 5")) {
						desk5Text.setText(p.getName() + " is dropping off 1 bag of " + p.getWeight() + "kg");
	            		if (p.excess_fee() != 0) {
	            			desk5Text.append("A buggage fee of " + p.excess_fee() + "$ is due");
	            			desk5Text.paintImmediately(desk5Text.getBounds());
	            		} else {
	            			desk5Text.append("\n" + "No baggage fee is due.");
	            			desk5Text.paintImmediately(desk5Text.getBounds());
	            		}
					}
	            	
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