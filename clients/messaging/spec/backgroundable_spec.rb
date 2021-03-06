require 'torquebox/messaging/backgroundable'

class MyTestModel
  include TorqueBox::Messaging::Backgroundable

  def an_async_action(arg1, arg2)
    a_sync_action
  end
  always_background :an_async_action

  def a_sync_action;  end
  def foo;  end
  def bar;  end
  def optioned; end
  def redefine_me; end
end

describe TorqueBox::Messaging::Backgroundable do

  describe "always_background" do
    it "should be able to handle mutliple methods" do
      MyTestModel.always_background :foo, :bar
      instance_methods = MyTestModel.instance_methods
      instance_methods.should include('__async_foo')
      instance_methods.should include('__async_bar')
    end

    it "should handle methods that are defined after the always_background call" do
      MyTestModel.always_background :baz
      MyTestModel.instance_methods.should_not include('__async_baz')
      MyTestModel.class_eval('def baz;end')
      MyTestModel.instance_methods.should include('__async_baz')
    end

    it "should handle methods that are redefined after the always_background call" do
      MyTestModel.always_background :redefine_me
      MyTestModel.class_eval('def redefine_me; :xyz; end')
      MyTestModel.new.__sync_redefine_me.should == :xyz
    end
    
    it "should work for private methods, maintaining visibility" do
      MyTestModel.class_eval('private; def no_peeking;end')
      MyTestModel.always_background :no_peeking
      MyTestModel.private_instance_methods.should include('no_peeking')
      MyTestModel.private_instance_methods.should include('__async_no_peeking')
      MyTestModel.private_instance_methods.should include('__sync_no_peeking')
    end

    it "should work for protected methods, maintaining visibility" do
      MyTestModel.class_eval('protected; def some_peeking;end')
      MyTestModel.always_background :some_peeking
      MyTestModel.protected_instance_methods.should include('some_peeking')
      MyTestModel.protected_instance_methods.should include('__async_some_peeking')
      MyTestModel.protected_instance_methods.should include('__sync_some_peeking')
    end
  end

  describe "a method handled asynchronously" do
    before(:each) do
      @queue = mock('queue')
      @queue.stub(:publish)
      TorqueBox::Messaging::Queue.stub(:new).and_return(@queue)
    end

    it "should put a message on the queue" do
      @queue.should_receive(:publish)
      MyTestModel.new.an_async_action(nil, nil)
    end

    it "should include the receiver, sync method, and args" do
      object = MyTestModel.new
      @queue.should_receive(:publish).with({:receiver => object, :method => '__sync_an_async_action', :args => [:a, :b]}, { })
      object.an_async_action(:a, :b) 
    end
    
    it "should not call the action immediately" do
      object = MyTestModel.new
      object.should_not_receive(:a_sync_action)
      object.an_async_action(nil, nil)
    end

    it "should pass through the options" do
      MyTestModel.always_background :optioned, :priority => :low
      @queue.should_receive(:publish).with(anything, :priority => :low)
      MyTestModel.new.optioned
    end

  end

  describe 'background' do
    before(:each) do
      @queue = mock('queue')
      @queue.stub(:publish)
      TorqueBox::Messaging::Queue.stub(:new).and_return(@queue)
      @object = MyTestModel.new
    end

    it "should queue any method called on it" do
      @queue.should_receive(:publish).with({:receiver => anything,
                                           :method => :foo,
                                             :args => anything}, { })
      @object.background.foo 
    end

    it "should queue the receiver" do
      @queue.should_receive(:publish).with({:receiver => @object,
                                           :method => anything,
                                             :args => anything}, { })
      @object.background.foo 
    end

    it "should queue the args" do
      @queue.should_receive(:publish).with({:receiver => anything,
                                           :method => anything,
                                             :args => [1,2]}, {})
      @object.background.foo(1,2) 
    end

    it "should raise when given a block" do
      lambda { 
        @object.background.foo { }
      }.should raise_error(ArgumentError)
    end

    it "should handle missing methods" do
      @object.should_receive(:method_missing)
      @object.background.no_method
    end

    it "should pass through any options" do
      @queue.should_receive(:publish).with({:receiver => anything,
                                           :method => anything,
                                             :args => anything},
                                           {:ttl => 1})
      @object.background(:ttl => 1).foo
    end
  end
  
end
