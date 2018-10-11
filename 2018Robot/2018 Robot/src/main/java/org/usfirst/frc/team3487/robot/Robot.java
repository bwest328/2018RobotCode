package org.usfirst.frc.team3487.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;

import net.bak3dnet.robotics.display.RevDigitDisplay;
import net.bak3dnet.robotics.display.modules.RapidDiagnosticsModule;
import net.bak3dnet.robotics.display.modules.BatteryPercentModule;

///////////////IOSetup//////////////
//	Drive Motors				  //
//	PWM 0 = Drive Motor Right 1	  //	
//	PWM 1 = Drive Motor Right 2	  //
//	PWM 2 = Drive Motor Left 1	  //
//	PWM 3 = Drive Motor Left 2    //
//								  //	
//	Other Motors				  //
//	PWM 4 = DropOff Motor		  //
//	PWM 5 = Lift Motor			  //
//	PWM 6 = Intake Motor 1		  //
//	PWM 7 = Intake Motor 2		  //
//								  //
//	DIO 0 = Bump Switch 0		  //
//	DIO 1 = Bump Switch 1		  //
////////////////////////////////////

///////////Button Setup/////////////
//	R_Joystick Moves Right	  	  //
//	L_Joystick Moves Left	  	  //
//	R_Button 2 Is Intake	  	  //
//	L_Button 2 Is Output	  	  //
//	R_Button 7 is Full Down		  //
//	R_Button 8 is Full Up		  //
//	R_Button 9 is Move Up 15	  //
//	R_Button 10 is Move Down 15	  //
////////////////////////////////////

public class Robot extends IterativeRobot {
	
	//Drive motors
	public Spark driveMotor1;
	public Spark driveMotor2;
	public Spark driveMotor3;
	public Spark driveMotor4;
	
	//Other Motors
	public Spark liftMotor;
	public Spark dropOffMotor;
	public Spark intakeMotor1;
	public Spark intakeMotor2;
	public Spark climbingWinch;
	public Spark climbingArm;
	
	//Joysticks
	public Joystick leftStick;
	public Joystick rightStick;
	
	//Timer
	public Timer moveTime = new Timer();
	public Timer reeTime = new Timer();
	
	//Lift Motor Variables
	boolean shouldMove15 = false;
	boolean shouldMoveN15 = false;
	boolean fullMoveUp = false;
	boolean fullMoveDown = false;
	boolean intakeMotor = false;
	
	//Start Switch
	DigitalInput leftStart;
	DigitalInput rightStart;
	DigitalInput middleStart;
	
	//Bump Switch Booleans
	boolean armMoveUp = false;
	boolean armMoveDown = false;
	
	boolean intakeOn = true;
	
	//Autonomous Variables
	private String gameData;
	boolean driveRight = false;
	public float autoMoveTime = 6;
	
	boolean middleStarting = false;
	boolean rightStarting = false;
	boolean leftStarting = false;
	
	@Override
	public void robotInit() {
		
		RevDigitDisplay display = RevDigitDisplay.getInstance();

		display.setActiveModule(new RapidDiagnosticsModule(new BatteryPercentModule(12D)));	
		
		leftStart = new DigitalInput(0);
		rightStart = new DigitalInput(2);
		
		if(leftStart.get() == false && rightStart.get() == false) {
			middleStarting = true;
		}
		
		if(leftStart.get() == true) {
			leftStarting = true;
		}
		
		if(rightStart.get() == true) {
			rightStarting = true;
		}
		
		
		CameraServer.getInstance().startAutomaticCapture();
		
		//Drive Motors
		driveMotor1 = new Spark(0);
		driveMotor2 = new Spark(1);
		driveMotor3 = new Spark(2);
		driveMotor4 = new Spark(3);
		
		//Other Sparks
		dropOffMotor = new Spark(4);
		liftMotor = new Spark(5);
		intakeMotor1 = new Spark(6);
		intakeMotor2 = new Spark(7);
		climbingArm = new Spark(8);
		climbingWinch = new Spark(9);
		
		//Joysticks
		leftStick = new Joystick(0);
		rightStick = new Joystick(1);
		
		//Bump Switches
		leftStart = new DigitalInput(0);
		middleStart = new DigitalInput(1);
		rightStart = new DigitalInput(2);
	}
	
	public void autonomousInit() {
		
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		
		if(leftStarting == true) {
		
			if(gameData.length() > 0) {
				moveTime.start();
				moveTime.reset();
				reeTime.start();
				reeTime.reset();
				if(gameData.charAt(0) == 'L') {
						driveRight = true;
				}
				else {
					driveRight = false;
				}
			}
		} else {
			
		}
		if(rightStarting == true) {
			
			if(gameData.length() > 0) {
				moveTime.start();
				moveTime.reset();
				reeTime.start();
				reeTime.reset();
				if(gameData.charAt(0) == 'R') {
					driveRight = true;
				}
				else {
					driveRight = false;
				}
			}
		} else {
			
		}
		if(middleStarting == true) { 
			moveTime.start();
			moveTime.reset();
			reeTime.start();
			reeTime.reset();
			if(moveTime.get() < 6) {
				
				driveMotor1.set(0.3);
				driveMotor2.set(0.3);
				
				driveMotor3.set(-0.3);
				driveMotor4.set(-0.3);
			}
		else {
				
				driveMotor1.set(0);
				driveMotor2.set(0);
				
				driveMotor3.set(0);
				driveMotor4.set(0);
			}
		}
	}
	
	public void autonomousPeriodic() {
		
		if(driveRight == true) {
			
			if(moveTime.get() < autoMoveTime) {
				
				driveMotor1.set(0.3);
				driveMotor2.set(0.3);
				
				driveMotor3.set(-0.3);
				driveMotor4.set(-0.3);
				
			}
			else {
				
				driveMotor1.set(0);
				driveMotor2.set(0);
				
				driveMotor3.set(0);
				driveMotor4.set(0);
				
				if(reeTime.get() > 6 && reeTime.get() < 8) {
					
					dropOffMotor.set(1);
				} else {
					dropOffMotor.set(0);
				}
			}
		}
		if(driveRight == false) {
			
			if(moveTime.get() < autoMoveTime) {
				
				driveMotor1.set(0.3);
				driveMotor2.set(0.3);
				
				driveMotor3.set(-0.3);
				driveMotor4.set(-0.3);
				
			}
			else {
				
				driveMotor1.set(0);
				driveMotor2.set(0);
				
				driveMotor3.set(0);
				driveMotor4.set(0);
				
				dropOffMotor.set(0);
			}
		}
	}
	
	//Calls Every 20 ms
	public void teleopInit() {
		
		moveTime.start();
		
	}
	
	@Override
	public void teleopPeriodic() {
		
		//Drive Motors
		//Right
		driveMotor1.set(rightStick.getY()/2);
		driveMotor2.set(rightStick.getY()/2);
		
		//Left
		driveMotor3.set(-leftStick.getY()/2);
		driveMotor4.set(-leftStick.getY()/2);
		
		////////////////
		//Arm Movement//
		////////////////
		
		
		//Move 15 Up
		if(leftStick.getRawButton(3) == true && shouldMove15 == false && shouldMoveN15 == false && armMoveUp == true) {
			shouldMove15 = true;
			moveTime.reset();
		}
		
		//Move 15 Down
		if(rightStick.getRawButton(4) == true && shouldMove15 == false && shouldMoveN15 == false && armMoveDown == true) {
			shouldMoveN15 = true;
			moveTime.reset();
		}
		
		//Move arm up in intervals
		if(shouldMove15 == true) {
			moveArm(15);
		}
		
		//Move arm down in intervals
		if(shouldMoveN15 == true) {
			moveArm(-15);
		}
		
		/////////////////
		//Output System//
		/////////////////
		
		//Move cube on top if pressed else stop
		if(leftStick.getRawButton(5)) {
			
			dropOffMotor.set(.5);
			
		}
		else if(rightStick.getRawButton(6) == false) {
			
			dropOffMotor.set(0);
			
		}
		
		//Move cube on top backwards if pressed else stop
		if(rightStick.getRawButton(6)) {
			
			dropOffMotor.set(-.5);
			
		}
		else if(leftStick.getRawButton(5) == false) {
			
			dropOffMotor.set(0);
			
		}
		
		////////////////////////
		//Cube Maneuver System//
		////////////////////////
		
		//Intake
		if(leftStick.getRawButton(2) == true) {
			
			inputOutput("output");
			
		}
		
		//Output
		if(rightStick.getRawButton(2) == true) {
			
			inputOutput("input");
			
		}
		
		if(rightStick.getRawButton(2) == false && leftStick.getRawButton(2) == false) {
			
			intakeMotor1.set(0);
			intakeMotor2.set(0);
			
		}

		if(rightStick.getRawButton(1) == true && leftStick.getRawButton(1) == false) {
			climbingArm.set(.5);
		} else if(rightStick.getRawButton(1) == false && leftStick.getRawButton(1) == true) {
			climbingArm.set(-.5);
		} else if (rightStick.getRawButton(1) == false && leftStick.getRawButton(1) == false) {
			climbingArm.set(0);
		}

		if (rightStick.getRawButton(12) == true && leftStick.getRawButton(12) == false) {
			climbingWinch.set(1);
		} else if(rightStick.getRawButton(12) == false && leftStick.getRawButton(12) == true) {
			climbingWinch.set(-1);
		} else if (rightStick.getRawButton(12) == false && leftStick.getRawButton(12) == false) {
			climbingWinch.set(0);
		}
	}
	
	//Move the arm in intervals
	public void moveArm(int angle) {
		
		//Up
		if (angle == 15) {
			liftMotor.set(0.5);
			
			if(moveTime.get() >= 0.075) {
				liftMotor.set(0);
				shouldMove15 = false; 
			}
		}
		
		//Down
		if (angle == -15) {
			liftMotor.set(-0.5);
			
			if(moveTime.get() >= 0.075) {
				liftMotor.set(0);
				shouldMoveN15 = false;
				
			}
		}
	}
	
	void inputOutput(String type) {
		
		if(type == "input") {
			
			intakeMotor = true;
			intakeMotor1.set(-1);
			intakeMotor2.set(1);
			
		}
		
		else if (type == "output") {
			
			intakeMotor = true;
			intakeMotor1.set(1);
			intakeMotor2.set(-1);
			
		}
	} 
}
