package com.ksw.remote.firestore.common

import android.net.Uri
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.ksw.base.common.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream


class FireBaseTask {
    suspend fun <T> getData(doc: DocumentReference, objectClass: Class<T>): ResultState<T> =
        withContext(Dispatchers.IO) {
            try {
                val snapShot = doc.get().await()

                if (snapShot.exists()) {
                    val result = snapShot.toObject(objectClass)!!
                    return@withContext ResultState.Success(result) as ResultState<T>
                } else {
                    return@withContext ResultState.EmptyDocument as ResultState<T>
                }
            } catch (e: FirebaseFirestoreException) {
                return@withContext ResultState.Error(e) as ResultState<T>
            }
        }

    suspend fun <C, M> getData(ref: C, objectClass: Class<M>): ResultState<List<M>> =
        withContext(Dispatchers.IO) {
            when (ref) {
                is CollectionReference -> {
                    try {
                        val snapShot = ref.get().await()
                        val result = snapShot.toObjects(objectClass)
                        return@withContext ResultState.Success(result) as ResultState<List<M>>
                    } catch (e: FirebaseFirestoreException) {
                        return@withContext ResultState.Error(e) as ResultState<List<M>>
                    }
                }

                is Query -> {
                    try {
                        val snapShot = ref.get().await()
                        val result = snapShot.toObjects(objectClass)
                        return@withContext ResultState.Success(result) as ResultState<List<M>>
                    } catch (e: FirebaseFirestoreException) {
                        return@withContext ResultState.Error(e) as ResultState<List<M>>
                    }
                }

                else -> ResultState.Error(Exception("Not found Reference"))
            }
        }

    suspend fun <T> setData(ref: T, data: Any): ResultState<Boolean> = withContext(Dispatchers.IO) {
        when (ref) {
            is DocumentReference -> {
                try {
                    val snapShot = ref.set(data).await()
                    val isResult = Tasks.forResult(snapShot).isSuccessful
                    return@withContext ResultState.Success(isResult)
                } catch (e: FirebaseFirestoreException) {
                    return@withContext ResultState.Error(e)
                }
            }

            is CollectionReference -> {
                try {
                    val snapShot = ref.add(data).await()
                    val isResult = Tasks.forResult(snapShot).isSuccessful
                    return@withContext ResultState.Success(isResult)
                } catch (e: FirebaseFirestoreException) {
                    return@withContext ResultState.Error(e)
                }
            }

            else -> ResultState.Error(Exception("Not found Reference"))
        }
    }

    suspend fun updateData(
        ref: DocumentReference,
        date: Any,
        fieldName: String
    ): ResultState<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val snapShot = ref.update(fieldName, date).await()
                return@withContext ResultState.Success(Tasks.forResult(snapShot).isSuccessful)
            } catch (e: FirebaseFirestoreException) {
                return@withContext ResultState.Error(e)
            }
        }

    suspend fun delete(ref: DocumentReference): ResultState<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val snapShot = ref.delete().await()
                return@withContext ResultState.Success(Tasks.forResult(snapShot).isSuccessful)
            } catch (e: FirebaseFirestoreException) {
                return@withContext ResultState.Error(e)
            }
        }


    suspend fun saveStorageImage(ref: StorageReference, file: File): ResultState<Uri> =
        withContext(Dispatchers.IO) {
            try {
                val uploadTask = ref.putStream(FileInputStream(file)).await()
                if (uploadTask.task.isSuccessful) {
                    return@withContext ResultState.Success(uploadTask.storage.downloadUrl.await())
                } else {
                    return@withContext ResultState.Error(Exception(""))
                }
            } catch (e: StorageException) {
                return@withContext ResultState.Error(e)
            }
        }

}