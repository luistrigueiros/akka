/*
 * Copyright (C) 2009-2017 Lightbend Inc. <http://www.lightbend.com>
 */
package akka.typed.javadsl;

import akka.typed.*;

import static akka.typed.javadsl.Actor.*;

public class JavaAlarmSample {


  interface GuardianMessage {}
  static final class CreateAlarm implements GuardianMessage {
    public final String password;
    public final ActorRef<JavaAlarmSample.Initialized> sender;
    public CreateAlarm(String password, ActorRef<JavaAlarmSample.Initialized> sender) {
      this.password = password;
      this.sender = sender;
    }
  }
  static final class Initialized {
    public final ActorRef<AlarmMessage> alarm;
    public Initialized(ActorRef<AlarmMessage> alarm) {
      this.alarm = alarm;
    }
  }

  interface AlarmMessage {}
  static final class EnableAlarm implements AlarmMessage {
    public final String password;
    public EnableAlarm(String password) {
      this.password = password;
    }
  }
  static final class DisableAlarm implements AlarmMessage {
    public final String password;
    public DisableAlarm(String password) {
      this.password = password;
    }
  }
  static final class ChangePassword implements AlarmMessage {
    public final String oldPassword;
    public final String newPassword;
    public ChangePassword(String oldPassword, String newPassword) {
      this.oldPassword = oldPassword;
      this.newPassword = newPassword;
    }
  }
  static final class Activity implements AlarmMessage {}



  static final Behavior<GuardianMessage> guardian = statefulBuilder(GuardianMessage.class)
    .match(CreateAlarm.class, (context, message) -> {
      final ActorRef<AlarmMessage> alarm = context.spawn(disabledAlarm(message.password), "alarm");
      message.sender.tell(new Initialized(alarm));
      // no ask for java yet, so lets interact from here
      alarm.tell(new Activity());
      alarm.tell(new EnableAlarm("secret"));
      alarm.tell(new Activity());
      alarm.tell(new EnableAlarm("secret"));
      alarm.tell(new DisableAlarm("secret"));
      alarm.tell(new Activity());
      alarm.tell(new ChangePassword("old", "new"));

      return same();
    }).build();

  static Behavior<AlarmMessage> disabledAlarm(final String password) {
    return statefulBuilder(AlarmMessage.class)
      .matchWithPredicate(EnableAlarm.class, msg -> msg.password.equals(password), (context, message) -> {
        System.out.println("Enabling alarm");
        return enabledAlarm(password);
      })
      .match(EnableAlarm.class, (context, message) -> {
        System.out.println("Trying to enable alarm with wrong password");
        return same();
      })
      .match(ChangePassword.class, (context, message) -> {
        if (message.oldPassword.equals(password)) {
          System.out.println("Password changed");
          return disabledAlarm(message.newPassword);
        } else {
          System.out.println("Wrong password, password not changed");

          return same();
        }
      })
      // alarm disabled, we don't care
      .match(Activity.class, (context, message) -> same())
      .matchAny((context, message) -> unhandled())
      .build();

  }

  static Behavior<AlarmMessage> enabledAlarm(String password) {
    return statefulBuilder(AlarmMessage.class)
      .match(Activity.class, (context, message) -> {
        System.out.println("Activity detected, oeoeoeoe!!!");
        return same();
      })
      .match(DisableAlarm.class, (context, message) -> {
        if (message.password.equals(password)) {
          System.out.println("Disabling alarm");
          return disabledAlarm(password);
        } else {
          System.out.println("Tried to disable alarm with wrong password");
          return same();
        }
      })
      .matchAny((ctx, msg) -> unhandled())
      .build();
  }

  public static void main(String[] args) {
    final ActorSystem<GuardianMessage> system = ActorSystem$.MODULE$.create("sysname", guardian);
    /* This doesn't compile though, for some reason:
     * cannot find symbol
     *   symbol:   method tell(akka.typed.javadsl.JavaAlarmSample.CreateAlarm)
     *   location: variable system of type akka.typed.ActorSystem<akka.typed.javadsl.JavaAlarmSample.GuardianMessage>
     *     system.tell(new CreateAlarm("secret", system.deadLetters()));
     */
    // system.tell(new CreateAlarm("secret", system.deadLetters()));
  }
}
