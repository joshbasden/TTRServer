package Command.ServerCommand;

/**
 * Created by jbasden on 2/4/19.
 */

public enum CommandType {
    S_CREATE_GAME,
    S_POLL,
    S_JOIN_GAME,
    S_LOGIN,
    S_REGISTER,
    S_SEND_MESSAGE,
    S_ASSIGN_FIRST_DEST,
    S_ASSIGN_DEST,
    S_DRAW_THREE_DESTINATION_CARDS_FROM_DRAW_PILE,
    S_CLAIM_ROUTE,
    S_DRAW_FACE_UP_TRAIN_CAR_CARD,
    S_DRAW_FROM_TRAIN_PILE,
    S_END_TURN,
    S_REPLACE_FACE_UP
}
