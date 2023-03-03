#!/bin/sh
TESTING=/opt/CLI-testing/2021
PRODUCTION=/opt/CLI-testing/2021-production
RULES=/opt/CLI-testing

#./scripts/execute_360x_testing.sh accept initiator $RULES $TESTING/qvera-accept $TESTING/qvera-accept-initiator-output

#./scripts/execute_360x_testing.sh accept initiator $RULES $TESTING/71028        $TESTING/71028-accept-initiator-output

#./scripts/execute_360x_testing.sh accept recipient $RULES $TESTING/71028        $TESTING/71028-accept-recipient-output

 ./scripts/execute_360x_testing.sh decline recipient $RULES $TESTING/Decline/msgs $TESTING/Decline/recipient-output





#./scripts/execute_360x_testing.sh accept initiator $RULES $PRODUCTION/qvera-request $PRODUCTION/qvera-request-output
