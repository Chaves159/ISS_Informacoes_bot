package informacoes;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class IssBot extends TelegramLongPollingBot {
	public void onUpdateReceived(Update update) {
		if(update.hasMessage() && update.getMessage().hasText()){
				JSONObject json;
				SendMessage message = new SendMessage().setChatId(update.getMessage().getChatId());
				if(update.getMessage().getText().equals("/start")){
					message.setText("Estão aqui alguns dos comandos deste bot!\n\n" + "/localizacao - Este comando tem a função de mostrar a localização atual da ISS!\n\n" + "/numero - Este comando mostra o número atual de tripulantes da ISS!\n" + "\n/tripulantes - Este comando mostra todos os tripulantes que estão atualmente em órbita na iss!\n");
				}
				try {
					execute(message);
				} catch (TelegramApiException e1) {
					e1.printStackTrace();
				}
				if(update.getMessage().getText().equals("/localizacao")){
					try {
						json = JsonReader.readJsonFromUrl("http://api.open-notify.org/iss-now.json");
						JSONObject iss = (JSONObject) json.get("iss_position");
						message.setText("Localização da Estação Espacial Internacional é: \n " + "Latitude: "
								+ (String) iss.get("latitude") + " \n Longitude: " + (String) iss.get("longitude"));
						execute(message);
	
					} catch(JSONException | IOException | TelegramApiException e) {
						e.printStackTrace();
					}
				}else if (update.getMessage().getText().equals("/numero")){
					try {
						json = JsonReader.readJsonFromUrl("http://api.open-notify.org/astros.json");
						
						message.setText(" Numero de Tripulantes: " + json.get("number"));
						execute(message);
					} catch (JSONException | IOException |TelegramApiException e) {
						e.printStackTrace();
					} 
				} else if(update.getMessage().getText().equals("/tripulantes")) {
					try {
						json = JsonReader.readJsonFromUrl("http://api.open-notify.org/astros.json");
						JSONArray iss = json.getJSONArray("people");
						int numeroTri = json.getInt("number");
						for(int i = 1; i <= numeroTri; i++) {
							JSONObject pessoa = iss.getJSONObject(i);
							message.setText(" Tripulante número " + i +": "  + pessoa.get("name"));
							execute(message);
						}
					
					
				} catch (JSONException | IOException | TelegramApiException e) {
					e.printStackTrace();
				} 
			 }
		}
	}

	
	
	public String getBotUsername() {

		return "ISS_informacoes_bot";
	}

	
	@Override
	public String getBotToken() {

		return "893446922:AAEzqPOvaOlNJFdQxXmn2iHlCNcclGZvQaA";
	}
}