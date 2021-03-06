package com.elbekD.bot

import com.elbekD.bot.http.TelegramApi
import com.elbekD.bot.types.CallbackQuery
import com.elbekD.bot.types.ChosenInlineResult
import com.elbekD.bot.types.InlineQuery
import com.elbekD.bot.types.InputMedia
import com.elbekD.bot.types.Message
import com.elbekD.bot.types.PreCheckoutQuery
import com.elbekD.bot.types.ShippingQuery
import com.elbekD.bot.types.Update
import java.io.File

interface Bot : TelegramApi {
    companion object {
        /**
         * @param username bot's username without leading @-symbol
         * @param token your bot token
         * @param pollingOptions options used to configure [LongPollingBot]
         */
        @JvmStatic
        fun createPolling(
            username: String,
            token: String,
            pollingOptions: PollingOptions.() -> Unit = { PollingOptions() }
        ): Bot {
            validateToken(token)
            return LongPollingBot(username, token, PollingOptions().apply(pollingOptions))
        }

        /**
         * @param username bot's username without leading @-symbol
         * @param token your bot token
         * @param webhookOptions options used to configure server and webhook params for [setWebhook] method
         */
        @JvmStatic
        fun createWebhook(
            username: String,
            token: String,
            webhookOptions: WebhookOptions.() -> Unit = { WebhookOptions() }
        ): Bot {
            validateToken(token)
            return WebhookBot(username, token, WebhookOptions().apply(webhookOptions))
        }

        private fun validateUsername(username: String) {
            if (username.isBlank() || username.startsWith("@"))
                throw IllegalArgumentException("Invalid username. Check and try again")
        }

        private fun validateToken(token: String) {
            if (token.isBlank())
                throw IllegalArgumentException("Invalid token. Check and try again")
        }
    }

    /**
     * Start the bot
     */
    fun start()

    /**
     * Stop the bot. Manual start needed
     */
    fun stop()

    /**
     * @param action called on [Message] update
     */
    fun onMessage(action: suspend (Message) -> Unit)

    /**
     * Removes [Message] update action
     */
    fun removeMessageAction()

    /**
     * @param action called if [Message] has been edited
     */
    fun onEditedMessage(action: suspend (Message) -> Unit)

    /**
     * Removes edited [Message] update action
     */
    fun removeEditedMessageAction()

    /**
     * @param action called on [channel post][Message] update
     */
    fun onChannelPost(action: suspend (Message) -> Unit)

    /**
     * Removes [channel post][Message] update action
     */
    fun removeChannelPostAction()

    /**
     * @param action called if [channel post][Message] has been edited
     */
    fun onEditedChannelPost(action: suspend (Message) -> Unit)

    /**
     * Removes [edited channel post][Message] update action
     */
    fun removeEditedChannelPostAction()

    /**
     * @param action called on [InlineQuery] update
     */
    fun onInlineQuery(action: suspend (InlineQuery) -> Unit)

    /**
     * Removes [InlineQuery] update action
     */
    fun removeInlineQueryAction()

    /**
     * @param action called on [chosen inline query][ChosenInlineResult] update
     */
    fun onChosenInlineQuery(action: suspend (ChosenInlineResult) -> Unit)

    /**
     * Removes [chosen inline query][ChosenInlineResult] update action
     */
    fun removeChosenInlineQueryAction()

    /**
     * @param action called on [CallbackQuery] update
     */
    fun onCallbackQuery(action: suspend (CallbackQuery) -> Unit)

    /**
     * Removes [CallbackQuery] update action
     */
    fun removeCallbackQueryAction()

    /**
     * @param action called on [ShippingQuery] update
     */
    fun onShippingQuery(action: suspend (ShippingQuery) -> Unit)

    /**
     * Removes [ShippingQuery] update action
     */
    fun removeShippingQueryAction()

    /**
     * @param action called on [PreCheckoutQuery] update
     */
    fun onPreCheckoutQuery(action: suspend (PreCheckoutQuery) -> Unit)

    /**
     * Removes [PreCheckoutQuery] update action
     */
    fun removePreCheckoutQueryAction()

    /**
     * @param command bot command which starts with `/`
     * @param action callback for the given `command` with [Message] parameter
     * and optional `argument` parameter
     *
     * @throws [IllegalArgumentException] if [command] exceeds constraints.
     * Check [Telegram Bot Commands](https://core.telegram.org/bots#commands)
     */
    fun onCommand(command: String, action: suspend (Message, String?) -> Unit)

    /**
     * @param data trigger provided via `callback_data` field of [InlineKeyboardButton][com.github.elbekD.bot.types.InlineKeyboardButton]
     * @param action callback for the given `data` with [CallbackQuery] parameter
     *
     * @throws [IllegalArgumentException] if [data] length not in `[1, 64]` range
     */
    fun onCallbackQuery(data: String, action: suspend (CallbackQuery) -> Unit)

    /**
     * @param query trigger provided via `query` field of [InlineQuery]
     * @param action callback for the given `query` with [InlineQuery] parameter
     *
     * @throws [IllegalArgumentException] if [query] length not in `[0, 512]` range
     */
    fun onInlineQuery(query: String, action: suspend (InlineQuery) -> Unit)

    /**
     * Triggered if no matching update handler found.
     */
    fun onAnyUpdate(action: suspend (Update) -> Unit)

    /**
     * Helper method to create photo media object
     * @param media file_id, url or file_attach_name
     */
    fun mediaPhoto(
        media: String,
        attachment: File? = null,
        caption: String? = null,
        parseMode: String? = null
    ): InputMedia

    /**
     * Helper method to create video media object
     * @param media file_id, url or file_attach_name
     */
    fun mediaVideo(
        media: String,
        attachment: File? = null,
        thumb: File? = null,
        caption: String? = null,
        parseMode: String? = null,
        width: Int? = null,
        height: Int? = null,
        duration: Int? = null,
        supportsStreaming: Boolean? = null
    ): InputMedia

    /**
     * Helper method to create animation media object
     * @param media file_id, url or file_attach_name
     */
    fun mediaAnimation(
        media: String,
        attachment: File? = null,
        thumb: File? = null,
        caption: String? = null,
        parseMode: String? = null,
        width: Int? = null,
        height: Int? = null,
        duration: Int? = null
    ): InputMedia

    /**
     * Helper method to create audio media object
     * @param media file_id, url or file_attach_name
     */
    fun mediaAudio(
        media: String,
        attachment: File? = null,
        thumb: File? = null,
        caption: String? = null,
        parseMode: String? = null,
        duration: Int? = null,
        performer: String? = null,
        title: String? = null
    ): InputMedia

    /**
     * Helper method to create document media object
     * @param media file_id, url or file_attach_name
     */
    fun mediaDocument(
        media: String,
        attachment: File? = null,
        thumb: File? = null,
        caption: String? = null,
        parseMode: String? = null
    ): InputMedia
}