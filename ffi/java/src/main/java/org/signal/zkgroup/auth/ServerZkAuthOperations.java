//
// Copyright (C) 2020 Signal Messenger, LLC.
// All rights reserved.
//
// SPDX-License-Identifier: GPL-3.0-only
//

// Generated by zkgroup/codegen/codegen.py - do not edit

package org.signal.zkgroup.auth;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.signal.zkgroup.InvalidInputException;
import org.signal.zkgroup.ServerSecretParams;
import org.signal.zkgroup.VerificationFailedException;
import org.signal.zkgroup.InvalidRedemptionTimeException;
import org.signal.zkgroup.ZkGroupError;
import org.signal.zkgroup.groups.GroupPublicParams;
import org.signal.zkgroup.internal.Native;
import org.signal.zkgroup.util.UUIDUtil;

public class ServerZkAuthOperations {

  private final ServerSecretParams serverSecretParams;

  public ServerZkAuthOperations(ServerSecretParams serverSecretParams) {
    this.serverSecretParams = serverSecretParams;
  }

  public AuthCredentialResponse issueAuthCredential(UUID uuid, int redemptionTime) {
    return issueAuthCredential(new SecureRandom(), uuid, redemptionTime);
  }

  public AuthCredentialResponse issueAuthCredential(SecureRandom secureRandom, UUID uuid, int redemptionTime) {
    byte[] newContents = new byte[AuthCredentialResponse.SIZE];
    byte[] random      = new byte[Native.RANDOM_LENGTH];

    secureRandom.nextBytes(random);

    int ffi_return = Native.serverSecretParamsIssueAuthCredentialDeterministicJNI(serverSecretParams.getInternalContentsForJNI(), random, UUIDUtil.serialize(uuid), redemptionTime, newContents);

    if (ffi_return != Native.FFI_RETURN_OK) {
      throw new ZkGroupError("FFI_RETURN!=OK");
    }

    try {
      return new AuthCredentialResponse(newContents);
    } catch (InvalidInputException e) {
      throw new AssertionError(e);
    }

  }

  public void verifyAuthCredentialPresentation(GroupPublicParams groupPublicParams, AuthCredentialPresentation authCredentialPresentation) throws VerificationFailedException, InvalidRedemptionTimeException {
       verifyAuthCredentialPresentation(groupPublicParams, authCredentialPresentation, System.currentTimeMillis());
     }
   
  public void verifyAuthCredentialPresentation(GroupPublicParams groupPublicParams, AuthCredentialPresentation authCredentialPresentation, long currentTimeMillis) throws VerificationFailedException, InvalidRedemptionTimeException {
    long acceptableStartTime = TimeUnit.MILLISECONDS.convert(authCredentialPresentation.getRedemptionTime()-1, TimeUnit.DAYS);
    long acceptableEndTime = TimeUnit.MILLISECONDS.convert(authCredentialPresentation.getRedemptionTime()+2, TimeUnit.DAYS);

    if (currentTimeMillis < acceptableStartTime || currentTimeMillis > acceptableEndTime) {
        throw new InvalidRedemptionTimeException(); 
    }

    int ffi_return = Native.serverSecretParamsVerifyAuthCredentialPresentationJNI(serverSecretParams.getInternalContentsForJNI(), groupPublicParams.getInternalContentsForJNI(), authCredentialPresentation.getInternalContentsForJNI());
    if (ffi_return == Native.FFI_RETURN_INPUT_ERROR) {
      throw new VerificationFailedException();
    }

    if (ffi_return != Native.FFI_RETURN_OK) {
      throw new ZkGroupError("FFI_RETURN!=OK");
    }
  }

}
