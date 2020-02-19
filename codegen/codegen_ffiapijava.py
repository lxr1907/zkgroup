
file_header = \
"""//
// Copyright (C) 2020 Signal Messenger, LLC.
// All rights reserved.
//
// SPDX-License-Identifier: GPL-3.0-only
//

// Generated by zkgroup/codegen/codegen.py - do not edit

#![allow(non_snake_case)]

use std::panic;

use crate::ffi::constants::*;

extern crate jni;

use super::simpleapi;

use jni::sys::jint;

// This is the interface to the JVM that we'll
// call the majority of our methods on.
use jni::JNIEnv;

// These objects are what you should use as arguments to your native function.
// They carry extra lifetime information to prevent them escaping this context
// and getting used after being GC'd.
use jni::objects::JClass;

// This is just a pointer. We'll be returning it from our function.
// We can't return one of the objects with lifetime information because the
// lifetime checker won't let us.
use jni::sys::jbyteArray;

fn u8toi8(input: Vec<u8>) -> Vec<i8> {
    let mut out: Vec<i8> = Default::default();
    for i in 0..input.len() {
        out.push(input[i] as i8);
    }
    out
}
"""

template_method_start = \
"""
#[no_mangle]
pub extern "system" fn Java_org_signal_zkgroup_internal_Native_%(function_name)sJNI(
    env: JNIEnv,
    _class: JClass,
"""

template_method_decl_end = \
""") -> i32 {
"""

template_method_body_start = \
    """    let result = panic::catch_unwind(|| {
"""

template_method_body_end = \
    """
    match result {
        Ok(result) => result,
        Err(_) => FFI_RETURN_INTERNAL_ERROR,
    }
"""

def get_args(params, commaAtEnd):
    s = ""
    for param in params:
        if param[0] != "int":
            s += "&" + param[1].snake() + ", "
        else:
            s += param[1].snake() + ", "

    if len(s) != 0 and not commaAtEnd:
        s = s[:-2]
    return s


def print_method(c, m, static):
    s = ""

    if c.wrap_class == None:
        class_name = c.class_name
    else:
        class_name = c.wrap_class

    function_name = class_name.lower_camel() + m.method_name.camel()
    s += template_method_start % {"function_name": function_name}

    # decl
    if not static:
        s += "    " + class_name.lower_camel() + ": jbyteArray,\n"
    for param in m.params:
        if param[0] != "int":
            s += "    " + param[1].lower_camel() + ": jbyteArray,\n"
        else:
            s += "    " + param[1].lower_camel() + ": jint,\n"
    if m.return_type != "boolean": 
        s += "    " + m.return_name.lower_camel() + "Out: jbyteArray,\n" 
    s += template_method_decl_end

    # body
    s += template_method_body_start

    if not static:
        s += "        let " + class_name.snake() + " = env.convert_byte_array(%s).unwrap();\n" % class_name.lower_camel()
    for param in m.params:
        if param[0] != "int":
            s += "        let " + param[1].snake() + " = env.convert_byte_array(%s).unwrap();\n" % param[1].lower_camel()
        else:
            s += "        let " + param[1].snake() + " = %s as u32;\n" % param[1].lower_camel()
    if m.return_type != "boolean":
        s += "        let mut %s: Vec<u8> = vec![0; env.get_array_length(%sOut).unwrap() as usize];\n" % (m.return_name.snake(), m.return_name.lower_camel())

    if not static:
        if m.return_type != "boolean":
            s += """\n        let ffi_return = simpleapi::%s_%s(&%s, %s &mut %s);\n""" % (class_name.camel(), m.method_name.lower_camel(), class_name.snake(), get_args(m.params, True), m.return_name.snake())
        else:
            s += """\n        let ffi_return = simpleapi::%s_%s(&%s, %s);\n""" % (class_name.camel(), m.method_name.lower_camel(), class_name.snake(), get_args(m.params, False))
    else:
        if m.return_type != "boolean":
            s += """\n        let ffi_return = simpleapi::%s_%s(%s &mut %s);\n""" % (class_name.camel(), m.method_name.lower_camel(), get_args(m.params, True), m.return_name.snake())
        else:
            s += """\n        let ffi_return = simpleapi::%s_%s(%s);\n""" % (class_name.camel(), m.method_name.lower_camel(), get_args(m.params, False))

    s += """        if ffi_return != FFI_RETURN_OK {
            return ffi_return;\n        }\n"""

    if m.return_type != "boolean":
        s += "\n        env.set_byte_array_region(%sOut, 0, &u8toi8(%s)[..]).unwrap();\n        FFI_RETURN_OK\n" % \
(m.return_name.lower_camel(), m.return_name.snake())
    else:
        s += "        FFI_RETURN_OK\n"

    s += "    });\n"

    s += template_method_body_end
    s += "}\n"

    return s

def print_class(c):
    s = ""
    for method in c.static_methods:
        s += print_method(c, method, True)
    for method in c.methods:
        s += print_method(c, method, False)
    return s


def produce_output(classes):
    s = file_header
    for c in classes:
        s += print_class(c)
    f = open("ffiapijava/ffiapijava.rs", "w")
    f.write(s)
    f.close()
