class Subject(object):
    """Observer pattern http://en.wikipedia.org/wiki/Observer_pattern
    """
    def __init__(self, *args, **kwargs):
        pass

    def register(self, observer):
        pass

    def unregister(self, observer):
        pass

    def notify_all(self, *args, **kwargs):
        pass

class  NotificationSubject(Subject):
    def __init__(self, *args, **kwargs):
        self._observers = []

    def register(self, observer):
        self._observers.append(observer)

    def unregister(self, observer):
        self._observers.remove(observer)

    def notify_all(self, message):
        for observer in self._observers:
            observer.notify(message)

    def send_message(self, message):
        print 'notification subject'
        self.notify_all(message)

class Observer(object):
    def __init__(self, *args, **kwargs):
        pass

    def notify(self, *args, **kwargs):
        pass


class EmailObserver(Observer):
    def notify(self, message):
        print '\n Send Email message'

class SMSObserver(Observer):
    def notify(self, message):
        print '\n Send SMS message'


if __name__ == '__main__':
    subject = NotificationSubject()
    email_observer = EmailObserver()
    subject.register(email_observer)
    sms_observer = SMSObserver()
    subject.register(sms_observer)
    subject.send_message('Hello world :-)')

