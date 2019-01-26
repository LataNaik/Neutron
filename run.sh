#!/bin/bash
mvn -Dapp_browser_url="https://egov-micro-qa.egovernments.org/" -Dtest="testcore.scenarios.ScriptFlows#" test
