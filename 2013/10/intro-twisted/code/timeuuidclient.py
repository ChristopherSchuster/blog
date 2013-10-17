from twisted.internet import reactor
from twisted.internet.protocol import ClientFactory
from twisted.protocols.basic import LineReceiver

class TimeUUIdClient(LineReceiver):

    welcome = 'Welcome to Time UUID generator :-)'

    def send_option(self, option):
        self.sendLine(option.replace(' ', ''))

    def ask_option(self):
        print 'press C to get a time UUID or X to exit\n'
        option = raw_input()
        if option.upper() == 'C':
            self.send_option(option.upper())
        elif option.upper() == 'X':
            print 'good bye :-)\n'
            self.transport.loseConnection()
        else:
            print 'invalid option good bye :-(\n'
            self.transport.loseConnection()

    def connectionMade(self):
        self.status = self.option_status['connected']

    def dataReceived(self, line):
        if self.welcome in  line :
            self.ask_option()
        else:
            print line
            self.ask_option()


class TimeUUIdClientFactory(ClientFactory):
    def startedConnecting(self, connector):
        print 'Started to connect.'

    def buildProtocol(self, addr):
        print 'Connected.'
        return TimeUUIdClient()

    def clientConnectionLost(self, connector, reason):
        reactor.stop()

    def clientConnectionFailed(self, connector, reason):
        reactor.stop()

if __name__ == '__main__':
    # create factory protocol and application
    factory = TimeUUIdClientFactory()

    # connect factory to this host and port
    reactor.connectTCP('localhost', 1320, factory)

    # run client
    reactor.run()
