//
// Copyright (C) 2020 Signal Messenger, LLC.
// All rights reserved.
//
// SPDX-License-Identifier: GPL-3.0-only
//

// Generated by zkgroup/codegen/codegen.py - do not edit

package org.signal.zkgroup.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public final class Native {

  public static final int FFI_RETURN_OK             = 0;
  public static final int FFI_RETURN_INTERNAL_ERROR = 1; // ZkGroupError
  public static final int FFI_RETURN_INPUT_ERROR    = 2;

  public static final int RANDOM_LENGTH = 32;

  static {
    try {
      String  osName    = System.getProperty("os.name").toLowerCase(java.util.Locale.ROOT);
      boolean isMacOs   = osName.startsWith("mac os x");
      String  extension = isMacOs ? ".dylib" : ".so";

      try (InputStream in = Native.class.getResourceAsStream("/libzkgroup" + extension)) {
        if (in != null) {
          copyToTempFileAndLoad(in, extension);
        } else {
          System.loadLibrary("zkgroup");
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Native() {
  }

  private static void copyToTempFileAndLoad(InputStream in, String extension) throws IOException {
    File tempFile = Files.createTempFile("resource", extension).toFile();
    tempFile.deleteOnExit();

    try (OutputStream out = new FileOutputStream(tempFile)) {

      copy(in, out);
    }
    System.load(tempFile.getAbsolutePath());
  }

  public static native int profileKeyGetCommitmentJNI(byte[] self, byte[] uuid, byte[] output);
  public static native int profileKeyGetProfileKeyVersionJNI(byte[] self, byte[] uuid, byte[] output);
  public static native int profileKeyCommitmentGetProfileKeyVersionJNI(byte[] self, byte[] output);
  public static native int profileKeyCommitmentCheckValidContentsJNI(byte[] self);
  public static native int groupSecretParamsGenerateDeterministicJNI(byte[] randomness, byte[] output);
  public static native int groupSecretParamsDeriveFromMasterKeyJNI(byte[] groupMasterKey, byte[] output);
  public static native int groupSecretParamsGetMasterKeyJNI(byte[] self, byte[] output);
  public static native int groupSecretParamsGetPublicParamsJNI(byte[] self, byte[] output);
  public static native int groupSecretParamsSignDeterministicJNI(byte[] self, byte[] randomness, byte[] message, byte[] output);
  public static native int groupSecretParamsCheckValidContentsJNI(byte[] self);
  public static native int groupSecretParamsEncryptUuidJNI(byte[] self, byte[] uuid, byte[] output);
  public static native int groupSecretParamsDecryptUuidJNI(byte[] self, byte[] uuidCiphertext, byte[] output);
  public static native int groupSecretParamsEncryptProfileKeyDeterministicJNI(byte[] self, byte[] randomness, byte[] profileKey, byte[] uuid, byte[] output);
  public static native int groupSecretParamsDecryptProfileKeyJNI(byte[] self, byte[] profileKeyCiphertext, byte[] uuid, byte[] output);
  public static native int groupSecretParamsEncryptBlobDeterministicJNI(byte[] self, byte[] randomness, byte[] plaintext, byte[] output);
  public static native int groupSecretParamsDecryptBlobJNI(byte[] self, byte[] blobCiphertext, byte[] output);
  public static native int serverSecretParamsGenerateDeterministicJNI(byte[] randomness, byte[] output);
  public static native int serverSecretParamsGetPublicParamsJNI(byte[] self, byte[] output);
  public static native int serverSecretParamsSignDeterministicJNI(byte[] self, byte[] randomness, byte[] message, byte[] output);
  public static native int serverSecretParamsCheckValidContentsJNI(byte[] self);
  public static native int serverPublicParamsReceiveAuthCredentialJNI(byte[] self, byte[] uuid, int redemptionTime, byte[] authCredentialResponse, byte[] output);
  public static native int serverPublicParamsCreateAuthCredentialPresentationDeterministicJNI(byte[] self, byte[] randomness, byte[] groupSecretParams, byte[] authCredential, byte[] output);
  public static native int serverPublicParamsCreateProfileKeyCredentialRequestContextDeterministicJNI(byte[] self, byte[] randomness, byte[] uuid, byte[] profileKey, byte[] output);
  public static native int serverPublicParamsReceiveProfileKeyCredentialJNI(byte[] self, byte[] profileKeyCredentialRequestContext, byte[] profileKeyCredentialResponse, byte[] output);
  public static native int serverPublicParamsCreateProfileKeyCredentialPresentationDeterministicJNI(byte[] self, byte[] randomness, byte[] groupSecretParams, byte[] profileKeyCredential, byte[] output);
  public static native int serverSecretParamsIssueAuthCredentialDeterministicJNI(byte[] self, byte[] randomness, byte[] uuid, int redemptionTime, byte[] output);
  public static native int serverSecretParamsVerifyAuthCredentialPresentationJNI(byte[] self, byte[] groupPublicParams, byte[] authCredentialPresentation);
  public static native int serverSecretParamsIssueProfileKeyCredentialDeterministicJNI(byte[] self, byte[] randomness, byte[] profileKeyCredentialRequest, byte[] uuid, byte[] profileKeyCommitment, byte[] output);
  public static native int serverSecretParamsVerifyProfileKeyCredentialPresentationJNI(byte[] self, byte[] groupPublicParams, byte[] profileKeyCredentialPresentation);
  public static native int groupPublicParamsGetGroupIdentifierJNI(byte[] self, byte[] output);
  public static native int groupPublicParamsVerifySignatureJNI(byte[] self, byte[] message, byte[] changeSignature);
  public static native int groupPublicParamsCheckValidContentsJNI(byte[] self);
  public static native int serverPublicParamsVerifySignatureJNI(byte[] self, byte[] message, byte[] notarySignature);
  public static native int serverPublicParamsCheckValidContentsJNI(byte[] self);
  public static native int authCredentialResponseCheckValidContentsJNI(byte[] self);
  public static native int authCredentialCheckValidContentsJNI(byte[] self);
  public static native int authCredentialPresentationGetUuidCiphertextJNI(byte[] self, byte[] output);
  public static native int authCredentialPresentationGetRedemptionTimeJNI(byte[] self, byte[] output);
  public static native int authCredentialPresentationCheckValidContentsJNI(byte[] self);
  public static native int profileKeyCredentialRequestContextGetRequestJNI(byte[] self, byte[] output);
  public static native int profileKeyCredentialRequestContextCheckValidContentsJNI(byte[] self);
  public static native int profileKeyCredentialRequestCheckValidContentsJNI(byte[] self);
  public static native int profileKeyCredentialResponseCheckValidContentsJNI(byte[] self);
  public static native int profileKeyCredentialCheckValidContentsJNI(byte[] self);
  public static native int profileKeyCredentialPresentationGetUuidCiphertextJNI(byte[] self, byte[] output);
  public static native int profileKeyCredentialPresentationGetProfileKeyCiphertextJNI(byte[] self, byte[] output);
  public static native int profileKeyCredentialPresentationCheckValidContentsJNI(byte[] self);
  public static native int uuidCiphertextCheckValidContentsJNI(byte[] self);
  public static native int profileKeyCiphertextCheckValidContentsJNI(byte[] self);

  private static void copy(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[4096];
    int read;

    while ((read = in.read(buffer)) != -1) {
      out.write(buffer, 0, read);
    }
  }
}
