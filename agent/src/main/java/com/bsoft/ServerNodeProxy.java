//package com.bsoft;
//
//
//import jdk.internal.org.objectweb.asm.*;
//import jdk.internal.org.objectweb.asm.tree.ClassNode;
//
//import javax.management.MBeanServer;
//import javax.management.MalformedObjectNameException;
//import javax.management.ObjectName;
//import javax.management.Query;
//import java.lang.management.ManagementFactory;
//import java.util.Set;
//
//import static jdk.internal.org.objectweb.asm.Opcodes.*;
//
///**
// * @author: 何胜豪
// * @Title: TODO
// * @Package: com.agent
// * @Description:
// * @date : 2021/7/3 15:24
// */
//public class ServerNodeProxy {
//
//
//    public byte[]  aop(byte[] bytes) {
//        return  aop(new ClassReader(bytes));
//    }
//
//    private byte[] aop(ClassReader cr)  {
//        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//
//        cr.accept(new ClassVisitor(Opcodes.ASM5, cw) {
//
//
//
//            @Override
//            public void visit(int i, int i1, String s, String s1, String s2, String[] strings) {
//                FieldVisitor fv;
//                {
//                    fv = cw.visitField(ACC_PRIVATE, "ip", "Ljava/lang/String;", null, null);
//                    fv.visitEnd();
//                }
//                {
//                    fv = cw.visitField(ACC_PRIVATE, "httpPath", "Ljava/lang/String;", null, null);
//                    fv.visitEnd();
//                }
//                {
//                    fv = cw.visitField(ACC_PUBLIC, "domainMapping", "Ljava/util/Map;", "Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;", null);
//                    fv.visitEnd();
//                }
//                {
//                    MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getDomainMapping", "()Ljava/util/Map;", "()Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;", null);
//                    mv.visitCode();
//                    Label l0 = new Label();
//                    mv.visitLabel(l0);
//                    mv.visitLineNumber(219, l0);
//                    mv.visitVarInsn(ALOAD, 0);
//                    mv.visitFieldInsn(GETFIELD, "ctd/net/rpc/logger/pm/ServerNode", "domainMapping", "Ljava/util/Map;");
//                    mv.visitInsn(ARETURN);
//                    Label l1 = new Label();
//                    mv.visitLabel(l1);
//                    mv.visitLocalVariable("this", "Lctd/net/rpc/logger/pm/ServerNode;", null, l0, l1, 0);
//                    mv.visitMaxs(1, 1);
//                    mv.visitEnd();
//                }
//                {
//                    MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "setDomainMapping", "(Ljava/util/Map;)V", "(Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;)V", null);
//                    mv.visitCode();
//                    Label l0 = new Label();
//                    mv.visitLabel(l0);
//                    mv.visitLineNumber(223, l0);
//                    mv.visitVarInsn(ALOAD, 0);
//                    mv.visitVarInsn(ALOAD, 1);
//                    mv.visitFieldInsn(PUTFIELD, "ctd/net/rpc/logger/pm/ServerNode", "domainMapping", "Ljava/util/Map;");
//                    Label l1 = new Label();
//                    mv.visitLabel(l1);
//                    mv.visitLineNumber(224, l1);
//                    mv.visitInsn(RETURN);
//                    Label l2 = new Label();
//                    mv.visitLabel(l2);
//                    mv.visitLocalVariable("this", "Lctd/net/rpc/logger/pm/ServerNode;", null, l0, l2, 0);
//                    mv.visitLocalVariable("domainMapping", "Ljava/util/Map;", "Ljava/util/Map<Ljava/lang/String;Ljava/lang/String;>;", l0, l2, 1);
//                    mv.visitMaxs(2, 2);
//                    mv.visitEnd();
//                }
//                {
//                    MethodVisitor mv  = cw.visitMethod(ACC_PUBLIC, "getIp", "()Ljava/lang/String;", null, null);
//                    mv.visitCode();
//                    Label l0 = new Label();
//                    mv.visitLabel(l0);
//                    mv.visitLineNumber(104, l0);
//                    mv.visitVarInsn(ALOAD, 0);
//                    mv.visitFieldInsn(GETFIELD, "ctd/net/rpc/logger/pm/ServerNode", "ip", "Ljava/lang/String;");
//                    mv.visitInsn(ARETURN);
//                    Label l1 = new Label();
//                    mv.visitLabel(l1);
//                    mv.visitLocalVariable("this", "Lctd/net/rpc/logger/pm/ServerNode;", null, l0, l1, 0);
//                    mv.visitMaxs(1, 1);
//                    mv.visitEnd();
//                }
//                {
//                    MethodVisitor mv  = cw.visitMethod(ACC_PUBLIC, "setIp", "(Ljava/lang/String;)V", null, null);
//                    mv.visitCode();
//                    Label l0 = new Label();
//                    mv.visitLabel(l0);
//                    mv.visitLineNumber(108, l0);
//                    mv.visitVarInsn(ALOAD, 0);
//                    mv.visitVarInsn(ALOAD, 1);
//                    mv.visitFieldInsn(PUTFIELD, "ctd/net/rpc/logger/pm/ServerNode", "ip", "Ljava/lang/String;");
//                    Label l1 = new Label();
//                    mv.visitLabel(l1);
//                    mv.visitLineNumber(109, l1);
//                    mv.visitInsn(RETURN);
//                    Label l2 = new Label();
//                    mv.visitLabel(l2);
//                    mv.visitLocalVariable("this", "Lctd/net/rpc/logger/pm/ServerNode;", null, l0, l2, 0);
//                    mv.visitLocalVariable("ip", "Ljava/lang/String;", null, l0, l2, 1);
//                    mv.visitMaxs(2, 2);
//                    mv.visitEnd();
//                }
//                {
//                    MethodVisitor mv  = cw.visitMethod(ACC_PUBLIC, "getHttpPath", "()Ljava/lang/String;", null, null);
//                    mv.visitCode();
//                    Label l0 = new Label();
//                    mv.visitLabel(l0);
//                    mv.visitLineNumber(112, l0);
//                    mv.visitVarInsn(ALOAD, 0);
//                    mv.visitFieldInsn(GETFIELD, "ctd/net/rpc/logger/pm/ServerNode", "httpPath", "Ljava/lang/String;");
//                    mv.visitInsn(ARETURN);
//                    Label l1 = new Label();
//                    mv.visitLabel(l1);
//                    mv.visitLocalVariable("this", "Lctd/net/rpc/logger/pm/ServerNode;", null, l0, l1, 0);
//                    mv.visitMaxs(1, 1);
//                    mv.visitEnd();
//                }
//                {
//                    MethodVisitor mv  = cw.visitMethod(ACC_PUBLIC, "setHttpPath", "(Ljava/lang/String;)V", null, null);
//                    mv.visitCode();
//                    Label l0 = new Label();
//                    mv.visitLabel(l0);
//                    mv.visitLineNumber(116, l0);
//                    mv.visitVarInsn(ALOAD, 0);
//                    mv.visitVarInsn(ALOAD, 1);
//                    mv.visitFieldInsn(PUTFIELD, "ctd/net/rpc/logger/pm/ServerNode", "httpPath", "Ljava/lang/String;");
//                    Label l1 = new Label();
//                    mv.visitLabel(l1);
//                    mv.visitLineNumber(117, l1);
//                    mv.visitInsn(RETURN);
//                    Label l2 = new Label();
//                    mv.visitLabel(l2);
//                    mv.visitLocalVariable("this", "Lctd/net/rpc/logger/pm/ServerNode;", null, l0, l2, 0);
//                    mv.visitLocalVariable("httpPath", "Ljava/lang/String;", null, l0, l2, 1);
//                    mv.visitMaxs(2, 2);
//                    mv.visitEnd();
//                }
//                super.visit(i, i1, s, s1, s2, strings);
//            }
//
//
//
//            @Override
//            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
//                if (!"instance".equals(name)) {
//                    return mv;
//                }
//                MethodVisitor aopMV = new MethodVisitor(super.api, mv) {
//                    @Override
//                    public void visitCode() {
//                        super.visitCode();
//                        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//                        mv.visitLdcInsn(">>>>>>>>>>>>>>>>>>>>>>>>>>> Proxy ServerNode instance action begin <<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//                        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//                    }
//
//                    @Override
//                    public void visitInsn(int opcode) {
//                        if (Opcodes.RETURN == opcode || ARETURN == opcode) {
//
//                            mv.visitMethodInsn(INVOKESTATIC, "ctd/util/AppContextHolder", "getRpcServerWorkUrl", "()Ljava/lang/String;", false);
//                            mv.visitVarInsn(ASTORE, 2);
//                            Label l27 = new Label();
//                            mv.visitLabel(l27);
//                            mv.visitLineNumber(87, l27);
//                            mv.visitVarInsn(ALOAD, 2);
//                            mv.visitLdcInsn("tcp://");
//                            mv.visitLdcInsn("");
//                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "replace", "(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;", false);
//                            mv.visitLdcInsn(":");
//                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "split", "(Ljava/lang/String;)[Ljava/lang/String;", false);
//                            mv.visitInsn(ICONST_0);
//                            mv.visitInsn(AALOAD);
//                            mv.visitVarInsn(ASTORE, 3);
//                            Label l28 = new Label();
//                            mv.visitLabel(l28);
//                            mv.visitLineNumber(89, l28);
//                            mv.visitTypeInsn(NEW, "ctd/mvc/resource/service/ResourceLocateService");
//                            mv.visitInsn(DUP);
//                            mv.visitMethodInsn(INVOKESPECIAL, "ctd/mvc/resource/service/ResourceLocateService", "<init>", "()V", false);
//                            mv.visitVarInsn(ASTORE, 4);
//                            Label l29 = new Label();
//                            mv.visitLabel(l29);
//                            mv.visitLineNumber(90, l29);
//                            mv.visitVarInsn(ALOAD, 4);
//                            mv.visitMethodInsn(INVOKEVIRTUAL, "ctd/mvc/resource/service/ResourceLocateService", "getHttpContextPath", "()Ljava/lang/String;", false);
//                            mv.visitVarInsn(ASTORE, 5);
//                            Label l30 = new Label();
//                            mv.visitLabel(l30);
//                            mv.visitLineNumber(92, l30);
//                            mv.visitVarInsn(ALOAD, 5);
//                            mv.visitLdcInsn("http://");
//                            mv.visitLdcInsn("");
//                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "replace", "(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;", false);
//                            mv.visitLdcInsn(":");
//                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "split", "(Ljava/lang/String;)[Ljava/lang/String;", false);
//                            mv.visitInsn(ICONST_1);
//                            mv.visitInsn(AALOAD);
//                            mv.visitVarInsn(ASTORE, 6);
//                            Label l31 = new Label();
//                            mv.visitLabel(l31);
//                            mv.visitLineNumber(94, l31);
//                            mv.visitFieldInsn(GETSTATIC, "ctd/net/rpc/logger/pm/ServerNode", "instance", "Lctd/net/rpc/logger/pm/ServerNode;");
//                            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
//                            mv.visitInsn(DUP);
//                            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
//                            mv.visitLdcInsn("http://");
//                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//                            mv.visitVarInsn(ALOAD, 3);
//                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//                            mv.visitLdcInsn(":");
//                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//                            mv.visitVarInsn(ALOAD, 6);
//                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
//                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
//                            mv.visitFieldInsn(PUTFIELD, "ctd/net/rpc/logger/pm/ServerNode", "httpPath", "Ljava/lang/String;"); Label l32 = new Label();
//                            mv.visitLabel(l32);
//                            mv.visitLineNumber(95, l32);
//                            mv.visitFieldInsn(GETSTATIC, "ctd/net/rpc/logger/pm/ServerNode", "instance", "Lctd/net/rpc/logger/pm/ServerNode;");
//                            mv.visitVarInsn(ALOAD, 3);
//                            mv.visitFieldInsn(PUTFIELD, "ctd/net/rpc/logger/pm/ServerNode", "ip", "Ljava/lang/String;");
//
//                            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//                            mv.visitLdcInsn(">>>>>>>>>>>>>>>>>>>>>>>>>>> Proxy ServerNode instance action end <<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
//                            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//
//                        }
//                        super.visitInsn(opcode);
//                    }
//                };
//                return aopMV;
//            }
//        }, ClassReader.SKIP_DEBUG);
//
//        return cw.toByteArray();
//    }
//}
