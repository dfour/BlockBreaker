package com.dfour.blockbreaker.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.dfour.blockbreaker.BBUtils;
import com.dfour.blockbreaker.BlockBreaker;
import com.dfour.blockbreaker.network.AbstractNetworkBase;
import com.dfour.blockbreaker.network.BBClient;
import com.dfour.blockbreaker.network.BBHost;
import com.dfour.blockbreaker.network.NetUser;

/**
 * @author darkd
 *
 */
public class MultiplayerScreen extends Scene2DScreen {
	private String externalIp = "Checking for IP....";
	private Label exip;
	private List<String> loggedUsers;
	private BBHost bbhost;
	private BBClient bbclient;
	private AbstractNetworkBase base;
	private TextButton btnHost; // start host
	private TextButton btnJoin; // join a game
	private TextButton btnReady;// ready up
	private TextButton btnSend; // send message
	private TextField txfMessageBox; // for holding written messages
	private TextField txfJoinip;
	private Label lblMessageWindow; // holds messages
	private String messages = "";
	private String messages_names = "";
	private float pingTimer = 2f;
	private Label lblMessageNameWindow;
	private Table chatWindow;
	private boolean isReady = false;

	// DONE limit to 4 connected players
	// DONE add ready up state for players
	// TODO make chat window more aesthetically pleasing
	// TODO add timer to count down after 2 or more players have started
	// DONE split message windows into names and message
	// TODO remove names from connected list when disconnection happens
	// TODO add client and host quit actions(quit from hosting/being joined)

	/*
	 * discover local server code InetAddress address =
	 * client.discoverHost(54777, 5000); System.out.println(address);
	 */

	public MultiplayerScreen(BlockBreaker p) {
		super(p);
		loggedUsers = new List<String>(skin);
		exip = new Label(externalIp, skin);

		btnHost = new TextButton("Host", skin);
		btnJoin = new TextButton("Join", skin);
		btnReady = new TextButton("Ready", skin);
		btnReady.setDisabled(true);
		btnSend = new TextButton("Send", skin);
		btnSend.setDisabled(true);
		txfMessageBox = new TextField("", skin);
		txfJoinip = new TextField("127.0.0.1", skin);
		lblMessageWindow = new Label("", skin, "chat");
		lblMessageNameWindow = new Label("", skin, "chat");

		chatWindow = new Table();
		chatWindow.add(lblMessageNameWindow).width(100);
		chatWindow.add(lblMessageWindow).expandX();

		btnHost.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				bbhost = new BBHost(parent.getPreferences().getUserName());
				base = bbhost;
				btnReady.setDisabled(false);
				btnSend.setDisabled(false);
				btnHost.setDisabled(true); // disable hosting and joining
				btnJoin.setDisabled(true);
				lblMessageNameWindow.setText("Server :");
				lblMessageWindow.setText("Creating Host on " + exip);
			}
		});

		btnSend.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if (txfMessageBox.getText() != null
						&& txfMessageBox.getText().length() > 0) {
					base.sendMessage(txfMessageBox.getText());
					System.out.println("Sending message: "
							+ txfMessageBox.getText());
					txfMessageBox.setText("");
				}
			}
		});

		btnJoin.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				// bbclient = new
				// BBClient(parent.getPreferences().getUserName());
				// debug version
				bbclient = new BBClient(BBUtils.generateRandomName());
				base = bbclient;
				bbclient.start(txfJoinip.getText());
				btnReady.setDisabled(false);
				btnSend.setDisabled(false);
				btnHost.setDisabled(true); // disable hosting and joining
				btnJoin.setDisabled(true);
				lblMessageWindow.setText("Attempting to Join host : "
						+ txfJoinip.getText());
			}
		});

		btnReady.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				base.readyUp(!isReady);
				isReady = !isReady;
				//addMessageToWindow("You:","User Readied up"); // get server to send this message
			}
		});
	}

	@Override
	public void show() {
		super.show();
		displayTable.add(exip).colspan(2);
		displayTable.add(btnHost).expandX();
		displayTable.row();
		displayTable.add(txfJoinip).colspan(2);
		displayTable.add(btnJoin).expandX();
		displayTable.row();
		displayTable.add(loggedUsers).height(200).width(150);
		displayTable.add(chatWindow).height(200).width(350).colspan(2);
		displayTable.row();
		displayTable.add(btnReady).expandX();
		displayTable.add(txfMessageBox);
		displayTable.add(btnSend).expandX();

		getExternalIpAddress();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		if (base != null) {
			base.update();
			updateMessages();
			checkPing(delta);
			fillUserList();
			isHostReady();
		}
	}

	private void isHostReady() {
		if (base.startLevelReady) {
			parent.base = base;
			this.isReturning = true;
			this.returnScreen = BlockBreaker.MULTIPLAYER_APPLICATION;
		}
	}
	
	private void fillUserList() {
		Array<String> users = new Array<String>();
		for (NetUser nu : base.characters.values()) {
			String ready = nu.isReady ? "R" : "X";
			users.add(ready + ":" + nu.ping + " " + nu.username);
		}
		loggedUsers.setItems(users);
	}

	private void checkPing(float delta) {
		if (base == null) {
			return;
		}
		pingTimer -= delta;
		if (pingTimer <= 0) {
			pingTimer = 2f;
			base.initiatePingCheck();
		}
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * Updates Messages from connected users
	 */
	private void updateMessages() {
		if (base.newMessage) {
			base.newMessage = false;
			addMessageToWindow(base.lastMessage.name,base.lastMessage.message);
		}
	}
	
	private void addMessageToWindow(String uname, String message){
		messages_names += "\n";
		messages_names += uname + ":";
		lblMessageNameWindow.setText(messages_names);

		messages += "\n";
		messages += message;
		lblMessageWindow.setText(messages);
	}

	/**
	 * Gets External IP address with get request to amazonaws
	 */
	public void getExternalIpAddress() {
		HttpRequest httpGet = new HttpRequest(HttpMethods.GET);
		httpGet.setUrl("http://checkip.amazonaws.com/");
		Gdx.net.sendHttpRequest(httpGet, new HttpResponseListener() {
			public void handleHttpResponse(HttpResponse httpResponse) {
				exip.setText(httpResponse.getResultAsString());
			}

			public void failed(Throwable t) {
				exip.setText("Unable to get IP");
			}

			@Override
			public void cancelled() {

			}
		});
	}
}
