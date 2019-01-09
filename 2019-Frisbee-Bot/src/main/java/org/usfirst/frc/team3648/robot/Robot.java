/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3648.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import org.usfirst.frc.team3648.robot.Buttons;
import edu.wpi.first.wpilibj.Compressor;

public class Robot extends TimedRobot {
	//Define the controller and port (0)
	private Joystick driveController = new Joystick(0);
	
	//Define Pneumatics
	
	Compressor compressor = new Compressor(0);
	
	Solenoid solenoid = new Solenoid(0);
	
	boolean enabled;
	
	//Define the spark (motor controller) for each wheel. The number in parentheses is the port it is on in the roboRio
	private Spark frontLeft = new Spark(0);
	private Spark backLeft = new Spark(1);
	private Spark frontRight = new Spark(2);
	private Spark backRight = new Spark(3);
	
	private Spark flywheel = new Spark(4);
	private Spark flywheel2 = new Spark(5);
	
	//To do tank drive we need to group the wheels on each side into a group
	private SpeedControllerGroup leftSide = new SpeedControllerGroup(frontLeft, backLeft);
	private SpeedControllerGroup rightSide = new SpeedControllerGroup(frontRight, backRight);
	
	//The drive chassis object. We will call the move commands for the wheels from this
	private DifferentialDrive drive = new DifferentialDrive(leftSide, rightSide);
	
	//Speed modifier
	private float motorMod = 1;
	
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		backLeft.setInverted(true);
		backRight.setInverted(true);
		frontRight.setInverted(true);
		
		enabled = compressor.enabled();
	}

	@Override
	public void autonomousInit() {
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		
	}

	/**
	 * This function is called periodically during operator control.
	 * @param axis 
	 */
	@Override
	public void teleopPeriodic() {
		//Drive the chassis at a speed decided by the joystick position * the speed modifier. 1 is full speed forward, -1 is full speed backward
		drive.tankDrive(driveController.getRawAxis(1)*motorMod, driveController.getRawAxis(5)*motorMod);
		
		//Change the speed modifier based on the trigger being held. Lets us have a slow, normal, and fast mode
		if(driveController.	getRawAxis(2)>0.5){
			motorMod = 0.7f;
		}else if(driveController.getRawAxis(3)>0.5){
			motorMod = 1f;
		}else{
			motorMod = 0.85f;
		}
		
		if(driveController.getRawButton(Buttons.X)){
			flywheel.set(-1);
			flywheel.set(-1);
		}else {
			flywheel.set(0);
		}

		if(driveController.getRawButton(Buttons.B)) {
			compressor.setClosedLoopControl(true);
		}else {
			compressor.setClosedLoopControl(false);
		
		}
		   
		if(driveController.getRawButton(4)) {
			solenoid.set(true);
		}
		
		if(driveController.getRawButton(1)) {
			solenoid.set(false);
		}
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
