package com.example.closetcompanion.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LoginWorker(
    private val context: Context,
    private val workerParams: WorkerParameters
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        var userDocument: DocumentSnapshot? = null

        val db = FirebaseFirestore.getInstance()

        Log.d("LoginWorker", "Starting DB Query")
        val queryUserName = await(db.collection("Users").whereEqualTo("username", workerParams.inputData.keyValueMap.getValue(WorkerKeys.USERNAME)).get())
        if(!queryUserName.isEmpty){
            userDocument = queryUserName.documents.find{ it.getString("username") == workerParams.inputData.keyValueMap.getValue(WorkerKeys.USERNAME)}

            val docP = userDocument!!.getString("password")
            val workP = workerParams.inputData.keyValueMap.getValue(WorkerKeys.PASSWORD)

            if(docP == workP){
                return Result.success(
                    workDataOf(
                        WorkerKeys.CORRECT_PASSWORD to "true",
                        WorkerKeys.USER_NOT_FOUND to "true",
                        WorkerKeys.USERNAME to userDocument.getString("username"),
                        WorkerKeys.PASSWORD to docP,
                        WorkerKeys.FIRST_NAME to userDocument.getString("first_name"),
                        WorkerKeys.LAST_NAME to userDocument.getString("last_name"),
                        WorkerKeys.EMAIL to userDocument.getString("email_address"),
                        WorkerKeys.DOB to userDocument.getString("dob")
                    )
                )
            }
            else{
                return Result.failure(
                    workDataOf(
                        WorkerKeys.CORRECT_PASSWORD to "false",
                        WorkerKeys.USER_NOT_FOUND to "true"
                    )
                )
            }
        }
        else{
            Log.d("LoginFragment", "Failed, queryUserName is null")
            return Result.failure(
                workDataOf(
                    WorkerKeys.CORRECT_PASSWORD to "false",
                    WorkerKeys.USER_NOT_FOUND to "true"
                )
            )
        }
    }
}