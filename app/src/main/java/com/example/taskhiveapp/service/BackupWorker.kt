package com.example.taskhiveapp.service

//class BackupWorker(
//    context: Context,
//    workerParameters: WorkerParameters,
//) : CoroutineWorker(context, workerParameters) {
//    override suspend fun doWork(): Result =
//        withContext(Dispatchers.IO) {
//            return@withContext try {
//                BackupRestoreDatabase(
//                    context = applicationContext,
//                    roomDatabase = AppDatabase.getInstance(applicationContext),
//                    externalBackupDir =
//                        File(
//                            applicationContext.getExternalFilesDir(null),
//                            "backups",
//                        ),
//                    databaseFile = File(applicationContext.getDatabasePath("TaskHive.db").path),
//                ).doBackup(5)
//                Result.success()
//            } catch (e: Exception) {
//                Result.failure(workDataOf("error" to e.localizedMessage))
//            }
//        }
//}
