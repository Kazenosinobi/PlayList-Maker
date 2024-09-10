package com.practicum.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.domain.api.SharingRepository

class SharingRepositoryImpl(private val context: Context) : SharingRepository {
    override fun share() {
        val url = context.getString(R.string.share_uri)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
        }
        val chooser = Intent.createChooser(intent, context.getString(R.string.messenger_choose))
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    override fun support() {
        val message = context.getString(R.string.support_message)
        val subject = context.getString(R.string.support_subject)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.support_mail)))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, message)
        context.startActivity(intent)
    }

    override fun termsOfUse() {
        val url = Uri.parse(context.getString(R.string.terms_uri))
        val intent = Intent(Intent.ACTION_VIEW, url)
        val chooser = Intent.createChooser(intent, context.getString(R.string.browser_choose))
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }
}