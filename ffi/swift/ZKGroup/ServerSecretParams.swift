//
// Copyright (C) 2020 Signal Messenger, LLC.
// All rights reserved.
//
// SPDX-License-Identifier: GPL-3.0-only
//
// Generated by zkgroup/codegen/codegen.py - do not edit

import Foundation
import libzkgroup

public class ServerSecretParams : ByteArray {

  public static let SIZE: Int = 896

  public static func generate() throws  -> ServerSecretParams {
    var randomness: [UInt8] = Array(repeating: 0, count: Int(32))
    let result = SecRandomCopyBytes(kSecRandomDefault, randomness.count, &randomness)
    guard result == errSecSuccess else {
      throw ZkGroupException.AssertionError
    }

    return try generate(randomness: randomness)
  }

  public static func generate(randomness: [UInt8]) throws  -> ServerSecretParams {
    var newContents: [UInt8] = Array(repeating: 0, count: ServerSecretParams.SIZE)

    let ffi_return = FFI_ServerSecretParams_generateDeterministic(randomness, UInt32(randomness.count), &newContents, UInt32(newContents.count))

    if (ffi_return != Native.FFI_RETURN_OK) {
      throw ZkGroupException.ZkGroupError
    }

    do {
      return try ServerSecretParams(contents: newContents)
    } catch ZkGroupException.IllegalArgument {
      throw ZkGroupException.AssertionError
    } 
  }

  public init(contents: [UInt8]) throws  {
    try super.init(newContents: contents, expectedLength: ServerSecretParams.SIZE, unrecoverable: true)

    
    let ffi_return = FFI_ServerSecretParams_checkValidContents(self.contents, UInt32(self.contents.count))

    if (ffi_return == Native.FFI_RETURN_INPUT_ERROR) {
      throw ZkGroupException.IllegalArgument
    }

    if (ffi_return != Native.FFI_RETURN_OK) {
      throw ZkGroupException.ZkGroupError
    }
  }

  public func getPublicParams() throws  -> ServerPublicParams {
    var newContents: [UInt8] = Array(repeating: 0, count: Int(ServerPublicParams.SIZE))

    let ffi_return = FFI_ServerSecretParams_getPublicParams(self.contents, UInt32(self.contents.count), &newContents, UInt32(newContents.count))

    if (ffi_return != Native.FFI_RETURN_OK) {
      throw ZkGroupException.ZkGroupError
    }

    return try ServerPublicParams(contents: newContents)
  }

  public func sign(message: [UInt8]) throws  -> NotarySignature {
    var randomness: [UInt8] = Array(repeating: 0, count: Int(32))
    let result = SecRandomCopyBytes(kSecRandomDefault, randomness.count, &randomness)
    guard result == errSecSuccess else {
      throw ZkGroupException.AssertionError
    }

    return try sign(randomness: randomness, message: message)
  }

  public func sign(randomness: [UInt8], message: [UInt8]) throws  -> NotarySignature {
    var newContents: [UInt8] = Array(repeating: 0, count: NotarySignature.SIZE)

    let ffi_return = FFI_ServerSecretParams_signDeterministic(self.contents, UInt32(self.contents.count), randomness, UInt32(randomness.count), message, UInt32(message.count), &newContents, UInt32(newContents.count))

    if (ffi_return != Native.FFI_RETURN_OK) {
      throw ZkGroupException.ZkGroupError
    }

    do {
      return try NotarySignature(contents: newContents)
    } catch ZkGroupException.InvalidInput {
      throw ZkGroupException.AssertionError
    }

  }

  public func serialize() -> [UInt8] {
    return contents
  }

}
