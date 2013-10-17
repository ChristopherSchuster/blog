from twisted.internet import reactor
from twisted.internet.protocol import Factory
from twisted.protocols.basic import LineReceiver

import datetime, time_uuid

class TimeUUId(LineReceiver):
    def __init__(self):
        self.options = {'C': self.handle_GETUUID, 'X': self.handle_CLOSE}

    def connectionMade(self):
        message = """
                  Welcome to Time UUID generator :-)\r\n
                  press C to get a time UUID
                  press X to exit
                  """
        self.transport.write(message)

    def lineReceived(self, line):

        if line.upper() in self.options:
            self.options[line.upper()]()
        else:
            self.transport.write('Invalid option :-( \n')

    def handle_CLOSE(self):
        self.transport.write('good bye :-) \n')
        self.transport.loseConnection()

    def handle_GETUUID(self):
        uuid =time_uuid.TimeUUID.with_utc(datetime.datetime.utcnow())
        self.transport.write(str(uuid))
        self.transport.write('\n')

class TimeUUIdFactory(Factory):

    protocol = TimeUUId

    def __init__(self):
        pass

    def buildProtocol(self, addr):
        return TimeUUId()

if __name__ == '__main__':
    reactor.listenTCP(1320, TimeUUIdFactory())
    reactor.run()
