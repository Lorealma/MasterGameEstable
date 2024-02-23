package com.example.mastergame.funcionesSecundarias

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.example.mastergame.R
import com.example.mastergame.databinding.ActivityCorversacionBinding
import com.zegocloud.zimkit.common.ZIMKitRouter
import com.zegocloud.zimkit.common.enums.ZIMKitConversationType
import com.zegocloud.zimkit.services.ZIMKit
import com.zegocloud.zimkit.services.model.ZIMKitGroupInfo
import im.zego.zim.entity.ZIMError
import im.zego.zim.entity.ZIMErrorUserInfo
import im.zego.zim.enums.ZIMErrorCode


class Corversacion : AppCompatActivity() {
  lateinit var binding: ActivityCorversacionBinding
  lateinit var linearLayout: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      binding = ActivityCorversacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

      binding.floatingBtn.setOnClickListener{view->
        enseñarmenu(view)

      }
    }
  fun enseñarmenu (view: View){
    val popupMenu = PopupMenu(this,view)
    popupMenu.inflate(R.menu.menu_chat)

    popupMenu.setOnMenuItemClickListener { item->
      when (item.itemId) {

        R.id.New_Chat ->{
          enseñarchat()


          true

        }
        R.id.crea_grupo ->{

          chatgrupo()


          true

        }
        R.id.entrar_grupo ->{
          entrargrupo()


          true

        }
        else -> true

      }
    }
    popupMenu.show()
  }
  fun enseñarchat(){
    val builder = AlertDialog.Builder(this)
    val editText = EditText(this)
    editText.hint = "userName"

    builder.setTitle("Nuevo chat")
    builder.setView(editText)

    builder.setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
      ZIMKitRouter.toMessageActivity(this, editText.text.toString(), ZIMKitConversationType.ZIMKitConversationTypePeer)
    }
    val dialog = builder.create()
    dialog.show()

  }

  fun chatgrupo(){
    val builder = AlertDialog.Builder(this)
    builder.setTitle("Nuevo grupo")

    val editText = EditText(this)
    editText.hint = "Grupo Id"
    val editText2 = EditText(this)
    editText2.hint = "Nombres Usuarios : separa con una coma los users"

    val linearLayout = LinearLayout(this)
    linearLayout.orientation = LinearLayout.VERTICAL
    linearLayout.addView(editText)
    linearLayout.addView(editText2)
    builder.setView(linearLayout)

    builder.setPositiveButton("OK") { dialogInterface: DialogInterface, i: Int ->
      val ids = editText2.text.toString().split(",")
      createGroupChat(ids, editText.text.toString())
    }

    builder.setNegativeButton("Cancelar", null)

    val dialog = builder.create()
    dialog.show()


  }
  fun entrargrupo(){
    val builder = AlertDialog.Builder(this)
    val editText = EditText(this)
    editText.hint = "Entrar en Grupo"

    builder.setTitle("nombre del grupo")
    builder.setView(editText)

    builder.setPositiveButton("OK") { dialogInterface: DialogInterface, _: Int ->
      joinGroupChat(editText.text.toString())
    }
    val dialog = builder.create()
    dialog.show()

  }
  fun joinGroupChat(groupId: String?) {
    ZIMKit.joinGroup(groupId) { groupInfo: ZIMKitGroupInfo, errorInfo: ZIMError ->
      if (errorInfo.code == ZIMErrorCode.SUCCESS) {
        // Enter the group chat page after joining the group chat successfully.
        ZIMKitRouter.toMessageActivity(
          this,
          groupInfo.id,
          ZIMKitConversationType.ZIMKitConversationTypeGroup
        )
      } else {
        // Implement the logic for the prompt window based on the returned error info when failing to join the group chat.
      }
    }
  }
  fun createGroupChat(ids: List<String?>?, groupName: String?) {
    if (ids == null || ids.isEmpty()) {
      return
    }
    ZIMKit.createGroup(
      groupName, ids
    ) { groupInfo: ZIMKitGroupInfo, inviteUserErrors: ArrayList<ZIMErrorUserInfo?>, errorInfo: ZIMError ->
      if (errorInfo.code == ZIMErrorCode.SUCCESS) {
        if (!inviteUserErrors.isEmpty()) {
          // Implement the logic for the prompt window based on your business logic when there is a non-existing user ID in the group.
        } else {
          // Directly enter the chat page when the group chat is created successfully.
          ZIMKitRouter.toMessageActivity(
            this,
            groupInfo.id,
            ZIMKitConversationType.ZIMKitConversationTypeGroup
          )
        }
      } else {
        // Implement the logic for the prompt window based on the returned error info when failing to create a group chat.
      }
    }
  }
}
