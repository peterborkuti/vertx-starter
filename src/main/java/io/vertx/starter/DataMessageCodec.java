package io.vertx.starter;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

public class DataMessageCodec implements MessageCodec<Data, Data> {

  /**
   * Called by Vert.x when marshalling a message to the wire.
   *
   * @param buffer the message should be written into this buffer
   * @param data   the message that is being sent
   */
  @Override
  public void encodeToWire(Buffer buffer, Data data) {
    JsonObject jsonToEncode = data.toJsonObject();

    // Encode object to string
    String jsonToStr = jsonToEncode.encode();

    // Length of JSON: is NOT characters count
    int length = jsonToStr.getBytes().length;

    // Write data into given buffer
    buffer.appendInt(length);
    buffer.appendString(jsonToStr);
  }

  /**
   * Called by Vert.x when a message is decoded from the wire.
   *
   * @param pos    the position in the buffer where the message should be read from.
   * @param buffer the buffer to read the message from
   * @return the read message
   */
  @Override
  public Data decodeFromWire(int pos, Buffer buffer) {
    // Length of JSON
    int length = buffer.getInt(pos);

    String jsonString = buffer.getString(pos + Integer.BYTES, pos + Integer.BYTES + length);

    return Data.createFromJsonString(jsonString);
  }

  /**
   * If a message is sent <i>locally</i> across the event bus, this method is called to transform the message from
   * the sent type S to the received type R
   *
   * @param data the sent message
   * @return the transformed message
   */
  @Override
  public Data transform(Data data) {
    return data;
  }

  /**
   * The codec name. Each codec must have a unique name. This is used to identify a codec when sending a message and
   * for unregistering codecs.
   *
   * @return the name
   */
  @Override
  public String name() {
    return this.getClass().getSimpleName();
  }

  /**
   * Used to identify system codecs. Should always return -1 for a user codec.
   *
   * @return -1 for a user codec.
   */
  @Override
  public byte systemCodecID() {
    return -1;
  }
}
