package com.jay.typicodeapp.features.home

import android.annotation.SuppressLint
import android.text.SpannedString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.recyclerview.widget.RecyclerView
import com.jay.typicodeapp.databinding.UserItemBinding
import com.jay.typicodeapp.services.data.UserData

class UsersListAdapter(
    private val onClickAction: ((UserData) -> Unit)? = null
) : RecyclerView.Adapter<UsersListAdapter.UserListItemViewHolder>() {

    private val users: MutableList<UserData> = mutableListOf()

    fun updateUsers(newUsers: List<UserData>) {
        users.clear()
        users.addAll(newUsers)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListItemViewHolder =
        UserListItemViewHolder(
            UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserListItemViewHolder, position: Int) {
        val user = users[position]

        holder.binding.run {
            val userName: SpannedString = buildSpannedString {
                bold {
                    append("First Last Name: ")
                }
                append(user.name)
            }

            tvName.text = userName
            tvEmail.text = "Email: ${user.email}"
            tvCity.text = "City: ${user.address.city}"
            tvCompany.text = "Company Name: ${user.company.name}"
        }

        if (onClickAction != null) {
            holder.itemView.setOnClickListener {
                onClickAction.invoke(user)
            }
        }
    }

    override fun getItemCount(): Int = users.size

    class UserListItemViewHolder(val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
