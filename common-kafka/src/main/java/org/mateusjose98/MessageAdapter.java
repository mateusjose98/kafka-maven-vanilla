package org.mateusjose98;

import com.google.gson.*;

import java.lang.reflect.Type;

public class MessageAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {
    @Override
    public JsonElement serialize(Message message, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", message.getPayload().getClass().getName());
        jsonObject.add("payload", jsonSerializationContext.serialize(message.getPayload()));
        jsonObject.add("correlationId", jsonSerializationContext.serialize(message.getId()));

        return jsonObject;
    }

    @Override
    public Message deserialize(JsonElement jsonElement,
                               Type type,
                               JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
       var payloadType = jsonElement.getAsJsonObject().get("type").getAsString();
       var correlationId = (CorrelationId) jsonDeserializationContext
               .deserialize(
                       jsonElement.getAsJsonObject().get("correlationId"), CorrelationId.class);
        try {
            var payload = jsonDeserializationContext.deserialize(jsonElement.getAsJsonObject().get("payload"), Class.forName(payloadType));
            return new Message(correlationId, payload);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }
}
