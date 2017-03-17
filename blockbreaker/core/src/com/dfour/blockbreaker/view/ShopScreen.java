package com.dfour.blockbreaker.view;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.dfour.blockbreaker.BBModel;
import com.dfour.blockbreaker.BlockBreaker;

public class ShopScreen extends Scene2DScreen{

	private BBModel bbModel;
	private TextButton btnExBall;
	private TextButton btnExLazer;
	private TextButton btnExGuide;
	private TextButton btnExMPower;
	private TextButton btnExMStrength;
	private TextButton btnExRechargeRate;
	private TextButton btnMagBallOnly;
	private Label lblExBall;
	private Label lblExLazer;
	private Label lblExGuide;
	private Label lblExMPower;
	private Label lblExMStrength;
	private Label lblExRechargeRate;
	private Label lblExMagBallOnly;
	private Label lblExBallc;
	private Label lblExLazerc;
	private Label lblExGuidec;
	private Label lblExMPowerc;
	private Label lblExMStrengthc;
	private Label lblExRechargeRatec;
	private Label lblExMagBallOnlyc;
	private Label lblCash;
	private Label lblScore;
	private TextButton btnDone;
	
	public ShopScreen (BlockBreaker p, BBModel m){
		super(p);
		bbModel = m;
	}


	@Override
	public void show() {
		super.show();
		// make button and labels for extra ball, longer lazer, longer guide, mag power, mag strength
		btnExBall = new TextButton("$"+calculateCost(bbModel.livesLeft,2.5,10), skin);
		btnExBall.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent e, float x, float y) {
				super.clicked(e, x, y);
				if(bbModel.removeCash(calculateCost(bbModel.livesLeft,2.5,10))){
					bbModel.livesLeft += 1;
					btnExBall.setText("$"+calculateCost(bbModel.livesLeft,2.5,10));
					updateCash();
				}
				btnExBall.setChecked(false);
			}
		});
		
		btnExLazer = new TextButton("$"+calculateCost(bbModel.baseLazerTimer,5,10), skin);
		btnExLazer.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				super.clicked(e, x, y);
				if(bbModel.removeCash(calculateCost(bbModel.baseLazerTimer,5,10))){
					bbModel.baseLazerTimer+=0.5f;
					btnExLazer.setText("$"+calculateCost(bbModel.baseLazerTimer,5,10));
					updateCash();
				}
				btnExLazer.setChecked(false);
			}
		});
		
		btnExGuide = new TextButton("$"+calculateCost(bbModel.baseGuideLazerTimer,2,10), skin);
		btnExGuide.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				super.clicked(e, x, y);
				if(bbModel.removeCash(calculateCost(bbModel.baseGuideLazerTimer,2,10))){
					bbModel.baseGuideLazerTimer+=0.5f;
					btnExGuide.setText("$"+calculateCost(bbModel.baseGuideLazerTimer,2,10));
					updateCash();
				}
				btnExGuide.setChecked(false);
			}
		});
		
		btnExMPower = new TextButton("$"+calculateCost((bbModel.baseMagnetPower/100),2,15), skin);
		btnExMPower.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				super.clicked(e, x, y);
				if(bbModel.removeCash(calculateCost((bbModel.baseMagnetPower/100),2,15))){
					bbModel.baseMagnetPower+=100;
					btnExMPower.setText("$"+calculateCost((bbModel.baseMagnetPower/100),2,15));
					updateCash();
				}
				btnExMPower.setChecked(false);
			}
		});
		
		btnExMStrength = new TextButton("$"+calculateCost((bbModel.baseMagnetStrength/10),2,15), skin);
		btnExMStrength.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				super.clicked(e, x, y);
				if(bbModel.removeCash(calculateCost((bbModel.baseMagnetStrength/10),2,15))){
					bbModel.baseMagnetStrength+=50;
					updateCash();
					btnExMStrength.setText("$"+calculateCost((bbModel.baseMagnetStrength/10),2,15));
				}
				btnExMStrength.setChecked(false);
			}
		});
		
		btnExRechargeRate = new TextButton("$"+calculateCost(bbModel.magnetRechargeRate*2500,1,2500), skin);
		btnExRechargeRate.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				super.clicked(e, x, y);
				if(bbModel.removeCash(calculateCost(bbModel.magnetRechargeRate*2500,1,2500))){
					bbModel.magnetRechargeRate+=1;
					updateCash();
					btnExRechargeRate.setText("$"+calculateCost(bbModel.magnetRechargeRate*2500,1,2500));
				}
				btnExRechargeRate.setChecked(false);
			}
		});
		
		btnMagBallOnly = new TextButton("$5000",skin);
		btnMagBallOnly.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if(bbModel.removeCash(5000)){
					bbModel.eternalMagBall = true;
					updateCash();
					btnMagBallOnly.setText("Purchased");
					lblExMagBallOnlyc.setText("Purchased");
					btnMagBallOnly.setDisabled(true);
				}
			}
			
		});
		
		btnDone = new TextButton("Done", skin);
		btnDone.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y){
				super.clicked(e, x, y);
				ShopScreen.this.returnScreen = BlockBreaker.APPLICATION;
				isReturning = true;	
				btnDone.setChecked(false);
			}
		});
		// label for score and cash
		lblScore = new Label("Score:"+bbModel.score,skin);
		lblCash = new Label("Cash: $"+bbModel.cash,skin);
		
		lblExBall = new Label("Extra Life",skin);
		lblExLazer = new Label("Longer Lasting Lazer Drill",skin);
		lblExGuide = new Label("Longer Lasting Guide",skin);
		lblExMPower = new Label("More Magnet Power Storage",skin);
		lblExMStrength = new Label("Stronger Magnets",skin);
		lblExRechargeRate = new Label("Faster Magnet Power Recharge",skin);
		lblExMagBallOnly = new Label("Magnetic Balls",skin);
		
		lblExBallc = new Label(bbModel.livesLeft+" Lives",skin);
		lblExLazerc = new Label(bbModel.baseLazerTimer+" Seconds",skin);
		lblExGuidec = new Label(bbModel.baseGuideLazerTimer+" Seconds",skin);
		lblExMPowerc = new Label(bbModel.baseMagnetPower+" Units",skin);
		lblExMStrengthc = new Label(bbModel.baseMagnetStrength+" Units",skin);
		lblExRechargeRatec = new Label(bbModel.magnetRechargeRate*60+" Units per Second",skin);
		if(bbModel.eternalMagBall){
			lblExMagBallOnlyc = new Label("Purchased",skin);
		}else{
			lblExMagBallOnlyc = new Label("Not Purchased",skin);
		}
		
		// label for cash
		updateCash();
		
        
        displayTable.add(lblExBall).uniformX().align(Align.left);
        displayTable.add(lblExBallc).uniformX().align(Align.right);
        displayTable.add().width(20f);
        displayTable.add(btnExBall);
        displayTable.row();
        displayTable.add(lblExLazer).uniformX().align(Align.left);
        displayTable.add(lblExLazerc).uniformX().align(Align.right);
        displayTable.add().width(20f);
        displayTable.add(btnExLazer);
        displayTable.row();
        displayTable.add(lblExGuide).uniformX().align(Align.left);
        displayTable.add(lblExGuidec).uniformX().align(Align.right);
        displayTable.add().width(20f);
        displayTable.add(btnExGuide);
        displayTable.row();
        displayTable.add(lblExMPower).uniformX().align(Align.left);
        displayTable.add(lblExMPowerc).uniformX().align(Align.right);
        displayTable.add().width(20f);
        displayTable.add(btnExMPower);
        displayTable.row();
        displayTable.add(lblExMStrength).uniformX().align(Align.left);
        displayTable.add(lblExMStrengthc).uniformX().align(Align.right);
        displayTable.add().width(20f);
        displayTable.add(btnExMStrength);
        displayTable.row();
        displayTable.add(lblExRechargeRate).uniformX().align(Align.left);
        displayTable.add(lblExRechargeRatec).uniformX().align(Align.right);
        displayTable.add().width(20f);
        displayTable.add(btnExRechargeRate);
        displayTable.row();
        displayTable.add(lblExMagBallOnly).uniformX().align(Align.left);
        displayTable.add(lblExMagBallOnlyc).uniformX().align(Align.right);
        displayTable.add().width(20f);
        displayTable.add(btnMagBallOnly);
        displayTable.row();
        displayTable.add(lblCash);
        displayTable.row();
        displayTable.add(lblScore);
        displayTable.row();
        displayTable.add(btnDone);
        
	}

	private void updateCash() {
		lblCash.setText("Cash: $"+bbModel.cash);
		lblScore.setText("Score: "+bbModel.score);
		lblExBallc.setText(bbModel.livesLeft+" Lives");
		lblExLazerc.setText(bbModel.baseLazerTimer+" Seconds");
		lblExGuidec.setText(bbModel.baseGuideLazerTimer+" Seconds");
		lblExMPowerc.setText(bbModel.baseMagnetPower+" Units");
		lblExMStrengthc.setText(bbModel.baseMagnetStrength+" Units");
		lblExRechargeRatec.setText(bbModel.magnetRechargeRate*60+" Units Per Second");
	}
	
	private int calculateCost(float multiplier, double mod, int base){
		return (int) Math.pow(multiplier,mod) + base;
	}


	@Override
	public void render(float delta) {
		super.render(delta);
		updateCash();
	}
}
